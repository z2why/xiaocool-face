import request from '@/utils/request'

export function getUrl() {
  return request({
    url: '/service/WebrtcWs/url',
    method: 'get'
  })
}
