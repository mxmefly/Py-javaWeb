/* 统一管理数据接口 */
const BASE_API = 'http://localhost:8100';
const LoginApi = BASE_API + '/login';
const LogoutApi = BASE_API + '/logout';
const getInfoApi =BASE_API+'/getInfo';
const SysMsg_Apis={
	getMsgNum:BASE_API+'/getMsgNum',
	getMsgData:BASE_API+'/getMsgData',
	updateMsgState:BASE_API+'/updateMsgState',
	updateStateAll:BASE_API+'/updateStateAll'
}
const Demo_Apis = {
    Test_Url: BASE_API + '/test',
    getUser: BASE_API + '/getUse',
    getTable: BASE_API + '/getTable'
}