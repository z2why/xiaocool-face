import time
import threading
try:
    from greenlet import getcurrent as get_ident
except ImportError:
    try:
        from thread import get_ident
    except ImportError:
        from _thread import get_ident


class CameraEvent(object):
    """An Event-like class that signals all active clients when a new frame is
    available.
    """
    def __init__(self):
        self.events = {}

    def wait(self):
        """Invoked from each client's thread to wait for the next frame."""
        ident = get_ident()
        if ident not in self.events:
            # this is a new client
            # add an entry for it in the self.events dict
            # each entry has two elements, a threading.Event() and a timestamp
            self.events[ident] = [threading.Event(), time.time()]
        return self.events[ident][0].wait()

    def set(self):
        """Invoked by the camera thread when a new frame is available."""
        now = time.time()
        remove = None
        for ident, event in self.events.items():
            if not event[0].isSet():
                # if this client's event is not set, then set it
                # also update the last set timestamp to now
                event[0].set()
                event[1] = now
            else:
                # if the client's event is already set, it means the client
                # did not process a previous frame
                # if the event stays set for more than 5 seconds, then assume
                # the client is gone and remove it
                if now - event[1] > 5:
                    remove = ident
        if remove:
            del self.events[remove]

    def clear(self):
        """Invoked from each client's thread after a frame was processed."""
        self.events[get_ident()][0].clear()


class BaseCamera(object):
    threads = {}  # store threads for different room codes
    frames = {}  # store frames for different room codes
    last_access = {}  # store last access time for different room codes
    events = {}  # store events for different room codes

    def __init__(self, code):
        """Start the background camera thread if it isn't running yet."""
        if code not in BaseCamera.threads:
            BaseCamera.last_access[code] = time.time()
            BaseCamera.events[code] = CameraEvent()

            # start background frame thread
            BaseCamera.threads[code] = threading.Thread(target=self._thread, args=(code,))
            BaseCamera.threads[code].start()

            # wait until frames are available
            while self.get_frame(code) is None:
                time.sleep(0)

    def get_frame(self, code):
        """Return the current camera frame."""
        BaseCamera.last_access[code] = time.time()

        # wait for a signal from the camera thread
        BaseCamera.events[code].wait()
        BaseCamera.events[code].clear()

        return BaseCamera.frames[code]

    @staticmethod
    def generate_frames(code):
        """Generator that returns frames from the camera."""
        raise RuntimeError('Must be implemented by subclasses.')

    @classmethod
    def _thread(cls, code):
        """Camera background thread."""
        print(f'Starting camera thread for room: {code}.')
        frames_iterator = cls.generate_frames(code)
        for frame in frames_iterator:
            BaseCamera.frames[code] = frame
            BaseCamera.events[code].set()  # send signal to clients
            time.sleep(0)

            # if there hasn't been any clients asking for frames in
            # the last 10 seconds then stop the thread
            if time.time() - BaseCamera.last_access[code] > 10:
                frames_iterator.close()
                print(f'Stopping camera thread for room {code} due to inactivity.')
                break

        del BaseCamera.threads[code]
        del BaseCamera.frames[code]
        del BaseCamera.last_access[code]
        del BaseCamera.events[code]
