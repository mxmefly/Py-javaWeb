<template>
	<div>
		<el-row :gutter="20">
			<el-col :span="24">
				<el-row :gutter="20" class="mgb20">
					<el-col :span="24">
						<el-card shadow="hover">
							<div slot="header" class="clearfix">
								<span>话题热度排行（No.10）</span>
							</div>
							<el-row class="mgb20">
								<el-col :span="16">
									<el-date-picker v-model="dateRange" type="daterange" align="right" unlink-panels range-separator="至"
									 start-placeholder="开始日期" end-placeholder="结束日期" :picker-options="pickerOptions" style="width: 100%;">
									</el-date-picker>
								</el-col>
								<el-col :span="8">
									<div>
										<el-button type="primary" icon="el-icon-search" style="float: right;" @click="handleSerch">查询</el-button>
									</div>
								</el-col>
							</el-row>
							<el-table v-loading="tableLoading" :data="userTableData" :row-class-name="tableRowClassName">
								</el-table-column>
								<el-table-column label="排名" prop="order" >
								</el-table-column>
								<el-table-column label="话题" prop="name" >
								</el-table-column>
								<el-table-column label="微博数" prop="counts" >
								</el-table-column>
								<el-table-column label="平均热度" prop="hotData" >
								</el-table-column>
								<el-table-column label="操作" width="200px">
									<template slot-scope="scope">
										<el-button size="mini" type="primary" @click="handleDetal(scope.row)">详细</el-button>
									</template>
								</el-table-column>
							</el-table>
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
		name: 'WeiboHotTopic',
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
				value: '',
				tableLoading: false,
				
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
					var data = [];
					var size = (this.userData.length > 10) ? 10 : this.userData.length;
					for (var i = 0; i < size; i++) {
						var arr = {
							order: i + 1,
							name: this.userData[i][0],
							counts: this.userData[i][1],
							hotData: this.userData[i][2]
						}
						data.push(arr)
					}
					return data
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
				this.tableLoading = true;
				if (this.dateRange == "") {
					this.handleSetDefaltTime();
				}
				var obj = {
					startTime: this.startTime.substring(0, 10),
					endTime: this.endTime.substring(0, 10)
				};
				this.$axios({
					method: 'post',
					url: BASE_API + '/getTopicOderData',
					data: obj
				}).then(function(res) {
					console.log("res", res)
					_this.userData = res.data.data;
					_this.tableLoading = false;
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

	/* .el-col {
		padding-left: 4px !important;
		padding-right: 4px !important;
		padding-top: 1px !important;
		padding-bottom: 1px !important;
	} */

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
