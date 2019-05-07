/* 统一管理数据接口 */
/* const BASE_API = 'http://localhost:8080/App-0.0.1-SNAPSHOT'; */
const BASE_API = 'http://localhost:8080';
/* const BASE_API = 'http://mxmefly.cn:8080/App'; */
const LoginApi = BASE_API + '/login';
const LogoutApi = BASE_API + '/logout';
const getInfoApi =BASE_API+'/getInfo';
const getDataSizeApi=BASE_API+'/getDataSize';
const getCpuUsdApi=BASE_API+'/getCpuUsd';
const getUserlineDateApi=BASE_API+'/getUserlineDate';
const getWeiboLineDataApi=BASE_API+'/getWeiboLineData';
const getWeiboCommentLineDataApi=BASE_API+'/getWeiboCommentLineData';
const touristLoginApi = BASE_API+'/touristLogin';
const SysMsg_Apis={
	getMsgNum:BASE_API+'/getMsgNum',
	getMsgData:BASE_API+'/getMsgData',
	updateMsgState:BASE_API+'/updateMsgState',
	updateStateAll:BASE_API+'/updateStateAll',
	delMsgById:BASE_API+'/delMsgById',
	delMsgByAll:BASE_API+'/delMsgByAll'
}
const Demo_Apis = {
    Test_Url: BASE_API + '/test',
    getUser: BASE_API + '/getUse',
    getTable: BASE_API + '/getTable'
}
const wordsImport = BASE_API+'/wordsImport';
const getWordsInfoApi=BASE_API+'/getWordsInfo'