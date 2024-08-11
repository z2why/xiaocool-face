import Vue from 'vue'
import Manage from "@/views/Manage";
import User from "@/views/User";
import File from "@/views/File";
import VueRouter from 'vue-router'
import AuditMeeting from "@/views/AuditMeeting";
import Meeting from "@/views/Meeting";
import AddMeeting from "@/components/AddMeeting";
import UserInfo from "@/components/UserInfo";
import MeetingList from "@/views/MeetingList";
import ApplyMeeting from "@/views/ApplyMeeting";
import RemindMessage from "@/views/RemindMessage";
import AllSign from "@/views/AllSign";
import MyInfo from "@/views/MyInfo";
import Sign from "@/views/Sign";
import join from "@/views/join";
import login from "@/views/Login";
import tabCreateMeeting from "@/views/tabCreateMeeting"
import ScheduledMeeting from "@/views/ScheduledMeeting";
import lt from "@/views/Login_Register"
import Home from "@/views/Home"


Vue.use(VueRouter)
const RouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (to) {
    return RouterPush.call(this, to).catch(err => err)
}
const RouterReplace = VueRouter.prototype.replace
VueRouter.prototype.replace = function replace (to) {
    return RouterReplace.call(this, to).catch(err => err)
}
const routes = [
    {
        path: '/',
        component: lt,
    },
    {
        name:'theMeeting',
        path: '/theMeeting',
        component:  () => import('@/views/meeting/index'),
    },
    {
        path: '/AddMeeting',
        name:'/AddMeeting',
        component: AddMeeting,
    },
    {
        path: '/login',
        name:'/login',
        component: login,
    },
    {
        name:'/Home',
        path: '/Home',
        component: Home,
    },

    {
        path: '/userinfo',
        component: UserInfo,
        children: [
            {
                path: '/',
                component: MyInfo,
            },

            {
                path: "meetinglist",
                component: MeetingList,
            },
            {
                path: "applymeeting",
                component: ApplyMeeting,
            },
            {
                path: "remindmessage",
                component: RemindMessage,
            },
            {
                path: "sign",
                component: Sign
            },
            {
                path: "ScheduledMeeting",
                component: ScheduledMeeting,
            },

            {   
                name: 'join',
                path: "join",
                component: join 
            },{

                name:'tabCreateMeeting',
                path: 'tabCreateMeeting',
                component: tabCreateMeeting
            }

        ],
    },


    {
        name:'AllSign',
        path: '/AllSign',
        component: AllSign
    },


    {
        path: '/Admin',
        name: 'Manage',
        component: Manage,
        children: [
            {
            path: 'user',
            name: 'User',
            component: User
        },
         {
            path: 'File',
            name: 'File',
            component: File,
        },
            {
             path: 'Meeting',
             name:'Meeting',
             component: Meeting,
            },
            {
                path: 'AuditMeeting',
                name:'AuditMeeting',
                component: AuditMeeting,
            },
        ]
    },

]


const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router
