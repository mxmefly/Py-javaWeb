<template>
	<div>
		<el-row :gutter="20">
			<el-col :span="24">
				<el-row :gutter="20" class="mgb20">
					<el-col :span="24">
						<el-card shadow="hover" class="mgb15" style="height:150px;">
							<div slot="header" class="clearfix">
								<span>条件查询</span>
							</div>
							<el-col :span="8">
								<div class="block">
									<el-date-picker v-model="dateRange" type="daterange" align="right" unlink-panels range-separator="至"
									 start-placeholder="开始日期" end-placeholder="结束日期" :picker-options="pickerOptions" style="width: 100%;">
									</el-date-picker>
								</div>
							</el-col>
							<el-col :span="6">
								<div>
									<el-input v-model="inputNickName" placeholder="请输入内容">
										<template slot="prepend">昵称</template>
									</el-input>
								</div>
							</el-col>
							<el-col :span="6">
								<div>
									<el-select v-model="value" placeholder="领域" disabled>
										<el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">>

										</el-option>
									</el-select>
								</div>
							</el-col>
							<el-col :span="4">
								<div>
									<el-button type="primary" icon="el-icon-search" style="float: right;" @click="handleSerch">查询</el-button>
								</div>
							</el-col>
						</el-card>
						<el-card shadow="hover">
							<div slot="header" class="clearfix">
								<span>微博用户热度排行（No.15）</span>
							</div>
							<el-table v-loading="tableLoading" :data="userTableData" :row-class-name="tableRowClassName">
								</el-table-column>
								<el-table-column label="排名" prop="order" width="70px">
								</el-table-column>
								<el-table-column label="昵称" prop="name" width="240px">
								</el-table-column>
								<el-table-column label="简介" prop="authentication" width="450px">
								</el-table-column>
								<el-table-column label="热度" prop="hotData" width="180px">
								</el-table-column>
								<el-table-column label="操作" width="200px">
									<template slot-scope="scope">
										<el-button size="mini" @click="handleGoHome(scope.row)">直达</el-button>
										<el-button size="mini" type="primary" @click="handleDetal(scope.row)">详细</el-button>
									</template>
								</el-table-column>
							</el-table>
						</el-card>
					</el-col>
				</el-row>
				<el-row :gutter="20" v-if="(selected.id!='')">
					<el-col :span="12">
						<el-card shadow="hover" style="height:500px;">
							<div slot="header" class="clearfix">
								<span>详细资料---{{selected.name}}</span>
							</div>
							<div class="user-info-list">签名：<span>{{selectedUserInfo.briefIntroduction}}</span></div>
							<div class="user-info-list">性别：<span>{{selectedUserInfo.gender}}</span></div>
							<div class="user-info-list">位置：<span>{{selectedUserInfo.province+selectedUserInfo.city}}</span></div>
							<div class="user-info-list">生日：<span>{{selectedUserInfo.birthday}}</span></div>
							<div class="user-info-list">微博：<span>{{selectedUserInfo.tweetsNum}}</span></div>
							<div class="user-info-list">关注：<span>{{selectedUserInfo.followsNum}}</span></div>
							<div class="user-info-list">粉丝：<span>{{selectedUserInfo.fansNum}}</span></div>
							<div class="user-info-list">简介：<span>{{selectedUserInfo.authentication}}</span></div>
							<div class="user-info-list">标签：<span>{{selectedUserInfo.labels}}</span></div>
						</el-card>
					</el-col>
					<el-col :span="12">
						<el-card shadow="hover" style="height:500px;">
							<div slot="header" class="clearfix">
								<span>人物画像</span>
							</div>
							<ve-wordcloud :data="wordsCloudData" :settings="chartSettings" v-loading="(wordsCloudData.rows.length==0)" :toolbox="toolbox"></ve-wordcloud>
						</el-card>
					</el-col>

				</el-row>
				<el-row :gutter="20" v-if="(selected.name!='')">
					<el-col :span="24">
						<el-card shadow="hover" style="height:70px;">
							<div class="moudelTitle">
								微博粉丝的地区分布和性别年龄段分布，可以<span>直观的分析该大V影响力受众人群</span>
								<br>由于权限问题样本容量只能获取到100左右，样本是具备随机性的，故也具备一定的分析参考性。
							</div>
						</el-card>
					</el-col>
					<el-col :span="12">
						<el-card shadow="hover" style="height:500px;">
							<div slot="header" class="clearfix">
								<span>粉丝地区分布</span>
							</div>
							<ve-map :data="fansPositionData" v-loading="(fansPositionData.rows.length==0)" :toolbox="toolbox"></ve-map>
						</el-card>
					</el-col>
					<el-col :span="12">
						<el-card shadow="hover" style="height:500px;">
							<div slot="header" class="clearfix">
								<span>粉丝性别/年龄分布</span>
							</div>
							<ve-pie :data="genderOrAgeData" :settings="chartSettings" v-loading="(genderOrAgeData.rows.length==0)" :toolbox="toolbox"></ve-pie>
						</el-card>
					</el-col>
				</el-row>
				<el-row :gutter="20" v-if="(selected.id!='')">
					<el-col :span="24">
						<el-card shadow="hover" style="height:100px;">
							<div class="moudelTitle">
								微博的情感倾向分布一定程度说明大V想塑造的形象，评论分布则体现粉丝对该大V的评价，两曲线差异与形象评价差异相关
								<br>范围0~1，越接近1倾向越积极，越接近0态度越消极，<span>曲线两边高中间低说明人物争议较大，反之表明人物多陈述客观事实</span>
								<br>微博情感倾向平均为： <span>{{sentiments1}}</span> ；评论情感倾向平均为 <span>{{sentiments2}}</span>
							</div>
						</el-card>
					</el-col>
					<el-col :span="12">
						<el-card shadow="hover" style="height:400px;">
							<div slot="header" class="clearfix">
								<span>微博情感倾向</span>
							</div>
							<ve-line :data="weiboSentimentData" :settings="chartSettings1" height="320px" v-loading="(weiboSentimentData.rows.length==0)" :toolbox="toolbox"></ve-line>
						</el-card>
					</el-col>
					<el-col :span="12">
						<el-card shadow="hover" style="height:400px;">
							<div slot="header" class="clearfix">
								<span>评论情感倾向</span>
							</div>
							<ve-line :data="fansSentimentData" :settings="chartSettings1" height="320px" v-loading="(fansSentimentData.rows.length==0)" :toolbox="toolbox"></ve-line>
						</el-card>
					</el-col>
				</el-row>
				<el-row :gutter="20" v-if="(selected.name!='')">
					<el-col :span="24">
						<el-card shadow="hover" style="height:360px;">
							<div slot="header" class="clearfix">
								<span>热度趋势及预测</span>
							</div>
							<ve-line :data="userHotlineDate" :data-zoom="dataZoom" height="280px" v-loading="(userHotlineDate.rows.length==0)" :toolbox="toolbox" :colors="colors"></ve-line>
						</el-card>
					</el-col>

				</el-row>


			</el-col>
		</el-row>
	</div>
</template>

<script>
	module.exports = {
		name: 'WeiboUser',
		data: function() {
			return {
				userData: [],
				tableData: [],
				pickerOptions: {
					shortcuts: [{
						text: '最近一周',
						onClick(picker) {
							const end = new Date();
							const start = new Date();
							start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
							picker.$emit('pick', [start, end]);
						}
					}, {
						text: '最近一个月',
						onClick(picker) {
							const end = new Date();
							const start = new Date();
							start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
							picker.$emit('pick', [start, end]);
						}
					}, {
						text: '最近三个月',
						onClick(picker) {
							const end = new Date();
							const start = new Date();
							start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
							picker.$emit('pick', [start, end]);
						}
					}]
				},
				dateRange: '',
				inputNickName: '',
				options: [{
					value: '选项1',
					label: '科技'
				}, {
					value: '选项2',
					label: '演绎'
				}],
				value: '',
				tableLoading: false,
				chartSettings: {
					level: [
						['70后', '80后', '90后', '00后', '其他'],
						['男', '女', '未知']
					]
				},
				wordsCloudData: {
					columns: ['word', 'count'],
					rows: []
				},
				selected: {
					id: "",
					name: ""
				},
				selectedUserInfo: {
					authentication: "",
					birthday: "",
					briefIntroduction: "",
					city: "",
					fansNum: "",
					followsNum: "",
					gender: "",
					labels: "",
					nickName: "",
					province: "",
					tweetsNum: "",
				},
				fansPositionData: {
					columns: ['位置', '人数'],
					rows: []
				},
				genderOrAgeData: {
					columns: ['数据', '人数'],
					rows: []
				},
				weiboSentimentData: {
					columns: ['情感值', '出现次数'],
					rows: []
				},
				chartSettings1: {
					stack: {
						'次数': ['出现次数']
					},
					area: true
				},
				fansSentimentData: {
					columns: ['情感值', '出现次数'],
					rows: []
				},
				sentiments1: 0,
				sentiments2: 0,
				userHotlineDate: {
					columns: ['时间', '热度', '预测'],
					rows: []
				},
				colors: ['#2f4554','#c23531',],
				dataZoom: [{
					type: 'slider',
				}],
				toolbox: {
					feature: {
						saveAsImage: {}
					}
				},
				type:0
			}
		},
		computed: {
			startTime: function() {
				if (this.dateRange == "") {
					var end = new Date();
					var start = new Date();
					start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
					this.dateRange = [start, end];
				}
				var time = this.formatDate(this.dateRange[0]);

				return time.substring(0, 10) + " 00:00:00";
			},
			endTime: function() {
				if (this.dateRange == "") {
					var end = new Date();
					var start = new Date();
					start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
					this.dateRange = [start, end];
				}
				return this.formatDate(this.dateRange[1]);
			},
			userTableData: function() {
				if (this.userData == []) {
					return []
				} else {
					if(this.type>0){
						var data = [];
						var size = (this.userData.length > 15) ? 15 : this.userData.length;
						for (var i = 0; i < size; i++) {
							var arr = {
								id: this.userData[i].weiboUsr._id,
								order: i + 1,
								name: this.userData[i].weiboUsr.nickName,
								authentication: this.userData[i].weiboUsr.authentication,
								hotData: this.userData[i].hotData
							}
							data.push(arr)
						}
						return data;                           
					}else{
						var data = [];
						var size = (this.userData.length > 15) ? 15 : this.userData.length;
						for (var i = 0; i < size; i++) {
							var arr = {
								id: this.userData[i][0],
								order: i + 1,
								name: this.userData[i][1],
								authentication: this.userData[i][2],
								hotData: this.userData[i][3]
							}
							data.push(arr)
						}
						return data
					}
				}
			},

		},
		created: function() {

			this.handleGetUserHotData();
		},
		activated: function() {

		},
		deactivated: function() {

		},
		methods: {
			handleSerch: function() {
				this.handleGetUserHotData()
			},
			//设置默认查询
			handleSetDefaltTime: function() {
				var end = new Date(2019, 03, 25, 00, 00, 00);
				var start = new Date();
				start.setTime(end.getTime() - 3600 * 1000 * 24 * 30);
				this.dateRange = [start, end];
			},
			formatDate: function(date) {
				var y = date.getFullYear();
				var m = date.getMonth() + 1;
				m = m < 10 ? ('0' + m) : m;
				var d = date.getDate();
				d = d < 10 ? ('0' + d) : d;
				var h = date.getHours();
				var minute = date.getMinutes();
				minute = minute < 10 ? ('0' + minute) : minute;
				var second = date.getSeconds();
				second = minute < 10 ? ('0' + second) : second;
				return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
			},
			tableRowClassName: function({
				row,
				rowIndex
			}) {
				if (rowIndex === 0) {
					return 'first-row';
				} else if (rowIndex === 1) {
					return 'secend-row';
				} else if (rowIndex == 2) {
					return 'third-row'
				} else
					return '';
			},
			handleGetUserHotData: function() {
				var _this = this;
				this.handleRefresh();
				this.tableLoading = true;
				if (this.dateRange == "") {
					this.handleSetDefaltTime();
				}
				var obj = {
					name: this.inputNickName,
					startTime: this.startTime,
					endTime: this.endTime
				};
				this.$axios({
					method: 'post',
					url: BASE_API + '/getUserHotData',
					data: obj
				}).then(function(res) {
					//console.log("res", res)
					_this.tableLoading = false
					_this.userData = res.data.data;
					if(_this.inputNickName.length>0){
						_this.type=1;
					}else{
						_this.type=0;
					}
					_this.selected.id = _this.userTableData[0].id;
					_this.selected.name = _this.userTableData[0].name;
					//_this.selectedUserInfo = _this.userData[0].weiboUsr;
					_this.handleGetUserInfo();
					_this.handleGetWordsCloudData();
					_this.handlegetFansInfo();
					_this.handleGetUserWeiboSetimentData();
					_this.handleGetFansWeiboSetimentData();
					_this.handleGetUserHotlineDate();
				}).catch(function() {
					console.log("请求失败");
				});
			},
			handleGoHome: function(row) {
				window.open("https://weibo.com/u/" + row.id);
			},
			handleDetal: function(row) {
				this.selected.id = row.id;
				this.selected.name = row.name;
				this.handleGetUserInfo();
				this.handleRefresh();
				this.handleGetWordsCloudData();
				this.handlegetFansInfo();
				this.handleGetUserWeiboSetimentData();
				this.handleGetFansWeiboSetimentData();
				this.handleGetUserHotlineDate();
			},
			handleGetUserInfo: function() {
				var _this = this;
				var obj = {
					id: this.selected.id,
				};
				this.$axios({
					method: 'post',
					url: BASE_API + '/getWeiboUserInfo',
					data: obj
				}).then(function(res) {
					_this.selectedUserInfo = res.data.data;
				}).catch(function() {
					Console.log("请求失败");
				});
			},
			handleGetWordsCloudData: function() {
				var _this = this;
				var obj = {
					id: this.selected.id,
					startTime: this.startTime.substring(0, 10),
					endTime: this.endTime.substring(0, 10)
				};
				this.$axios({
					method: 'post',
					url: BASE_API + '/getUserPortrait',
					data: obj
				}).then(function(res) {
					_this.wordsCloudData.rows = res.data.data;
				}).catch(function() {
					Console.log("请求失败");
				});
			},
			handlegetFansInfo: function() {
				var _this = this;
				var obj = {
					id: this.selected.id
				};
				this.$axios({
					method: 'post',
					url: BASE_API + '/getFansInfo',
					data: obj
				}).then(function(res) {
					_this.fansPositionData.rows = res.data.data.locationList;
					_this.genderOrAgeData.rows = res.data.data.genderOrAgeList;
				}).catch(function() {
					Console.log("请求失败");
				});
			},
			handleGetUserWeiboSetimentData: function() {
				var _this = this;
				var obj = {
					id: this.selected.id,
					startTime: this.startTime,
					endTime: this.endTime
				};
				this.$axios({
					method: 'post',
					url: BASE_API + '/getUserWeiboSetimentData',
					data: obj
				}).then(function(res) {
					_this.weiboSentimentData.rows = res.data.data.dataList;
					_this.sentiments1 = res.data.data.avg;
				}).catch(function() {
					Console.log("请求失败");
				});
			},
			handleGetFansWeiboSetimentData: function() {
				var _this = this;
				var obj = {
					id: this.selected.id,
					startTime: this.startTime,
					endTime: this.endTime
				};
				this.$axios({
					method: 'post',
					url: BASE_API + '/getFansWeiboSetimentData',
					data: obj
				}).then(function(res) {
					_this.fansSentimentData.rows = res.data.data.dataList;
					_this.sentiments2 = res.data.data.avg;
				}).catch(function() {
					Console.log("请求失败");
				});
			},
			handleGetUserHotlineDate: function() {
				var _this = this;
				var obj = {
					id: this.selected.id,
					startTime: this.startTime.substring(0, 10),
					endTime: this.endTime.substring(0, 10)
				};
				this.$axios({
					method: 'post',
					url: BASE_API + '/getUserHotlineDate',
					data: obj
				}).then(function(res) {
					_this.userHotlineDate.rows = res.data.data;

				}).catch(function() {
					console.log("请求失败");
				});
			},
			handleRefresh: function() {
				this.wordsCloudData.rows = [];
				this.fansPositionData.rows = [];
				this.genderOrAgeData.rows = [];
				this.weiboSentimentData.rows = [];
				this.fansSentimentData.rows = [];
				this.userHotlineDate.rows = [];
			}

		}
	}
</script>


<style scoped>
	.el-row {
		margin-bottom: 15px;
	}

	.el-col {
		padding-left: 4px !important;
		padding-right: 4px !important;
		padding-top: 1px !important;
		padding-bottom: 1px !important;
	}

	.mgb20 {
		margin-bottom: 20px;
	}

	.mgb15 {
		margin-bottom: 15px;
	}

	.moudelTitle {
		color: #1482F0
	}

	.moudelTitle span {
		color: #E6A23C;
		font-size: 18px;
	}

	.el-table .first-row {
		background: #ef3d2f;
		color: #0D2650;
		font-size: 17px;
	}

	.el-table .secend-row {
		background: #e3e8ec;
		font-size: 16px;
	}

	.el-table .third-row {
		background: #f1f2f6;
		font-size: 15px;
	}

	.user-info-list {
		font-size: 16px;
		color: #303133;
		line-height: 25px;
		margin-bottom: 22px;
	}

	.user-info-list span {
		margin-left: 70px;
		display: inline-block;
	}
</style>
