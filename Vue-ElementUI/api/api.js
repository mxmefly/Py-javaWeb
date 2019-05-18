/* 统一管理数据接口 */
/* const BASE_API = 'http://localhost:8080/App-0.0.1-SNAPSHOT'; */
const BASE_API = 'http://localhost:8080';
/* 登录 */
const LoginApi = BASE_API + '/login';
/* 访客登录 */
const touristLoginApi = BASE_API+'/touristLogin';
/* 登出 */
const LogoutApi = BASE_API + '/logout';
/* 拉取用户信息 */
const getInfoApi =BASE_API+'/getInfo';
/* 获取首页信息规模 */
const getDataSizeApi=BASE_API+'/getDataSize';
/* 获取当前服务器CPU负载 */
const getCpuUsdApi=BASE_API+'/getCpuUsd';
/* 获取首页微博用户爬取日曲线 */
const getUserlineDateApi=BASE_API+'/getUserlineDate';
/* 获取首页微博爬取日曲线 */
const getWeiboLineDataApi=BASE_API+'/getWeiboLineData';
/* 获取首页微博评论爬取日曲线 */
const getWeiboCommentLineDataApi=BASE_API+'/getWeiboCommentLineData';
/* 消息管理相关方法 */
const SysMsg_Apis={
	getMsgNum:BASE_API+'/getMsgNum',
	getMsgData:BASE_API+'/getMsgData',
	updateMsgState:BASE_API+'/updateMsgState',
	updateStateAll:BASE_API+'/updateStateAll',
	delMsgById:BASE_API+'/delMsgById',
	delMsgByAll:BASE_API+'/delMsgByAll'
}
/*Demo 相关请求 */
const Demo_Apis = {
    Test_Url: BASE_API + '/test',
    getUser: BASE_API + '/getUse',
    getTable: BASE_API + '/getTable'
}
/* 导入词库 */
const wordsImportApi = BASE_API+'/wordsImport';
/* 词库单词信息 */
const getWordsInfoApi=BASE_API+'/getWordsInfo'
/* 分页获取基础词库 */
const getWordListApi=BASE_API+'/getWordList'
/* 改变单词的是否显示状态 */
const editWordShowApi=BASE_API+'/editWordShow'
/* 删除单词 */
const deleteWordOneApi=BASE_API+'/deleteWordOne'
/* 获取新词库 分页查询 */
const getNewWordListApi=BASE_API+'/getNewWordList'
/* 新词词库导入基础词库 */
const newWordImportApi=BASE_API+'/newWordImport'
/* 删除新词库单词 */
const deleteNewWordOneApi=BASE_API+'/deleteNewWordOne'

/* 获取用户热度排行 */
const getUserHotDataApi=BASE_API + '/getUserHotData'
/* 获取用户信息 */
const getWeiboUserInfoApi=BASE_API + '/getWeiboUserInfo'
/* 获取一定时间内的某用户词云 */
const getUserPortraitapi=BASE_API + '/getUserPortrait'
/* 获取大V的粉丝分布 */
const getFansInfoApi=BASE_API + '/getFansInfo'
/* 获取大V 一段时间内微博的情感分布 */
const getUserWeiboSetimentDataApi=BASE_API + '/getUserWeiboSetimentData'
/* 获取 大V粉丝 一段时间内微博评论的情感分布*/
const getFansWeiboSetimentDataApi=BASE_API + '/getFansWeiboSetimentData'
/* 获取大V热度曲线 */
const getUserHotlineDateApi=BASE_API + '/getUserHotlineDate'

/* 获取话题排行 */
const getTopicOderDataApi=BASE_API + '/getTopicOderData'
/* 获取话题活跃用户 */
const getTopicUserApi=BASE_API + '/getTopicUser'
/* 获取话题微博 */
const getTopicUserWeiboInfoApi=BASE_API + '/getTopicUserWeiboInfo'
/* 获取话题热度曲线 */
const getTopicHotlineDateApi=BASE_API + '/getTopicHotlineDate'

/* 抽样获取粉丝信息 */
const getAllUserInfoApi=BASE_API + '/allData/getUserInfo'

/* 获取 表情/字词 排行 */
const getHotBaseDataApi= BASE_API + '/getHotBaseData'
/* 获取常用此表情/字词的常用用户 */
const getBaseDataUserApi = BASE_API + '/getBaseDataUser'
/* 获取常用字词/表情的热度曲线 */
const getWordHotLineApi = BASE_API + '/getWordHotLine'
/* 获取过去几年的热词 */
const getPassYearHotWordApi=BASE_API + '/getPassYearHotWord'

/* 获取配置表信息 */
const getConfigApi=BASE_API + '/getConfig'
/* 获取事件、过程的状态 */
const getEventConfigDateApi=BASE_API + '/getEventConfigDate'
/* 获取过程执行日志 */
const getLogTableApi =BASE_API + '/getLogTable'
/* 保存配置信息 */
const saveSpiderConfigApi=BASE_API + '/saveSpiderConfig'
