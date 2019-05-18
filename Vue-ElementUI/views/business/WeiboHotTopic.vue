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
							<el-table v-loading="tableLoading" :data="topicTableData" :row-class-name="tableRowClassName">
								</el-table-column>
								<el-table-column label="排名" prop="order" >
								</el-table-column>
								<el-table-column label="话题" prop="name" :show-overflow-tooltip="true">
								</el-table-column>
								<el-table-column label="微博数" sortable prop="counts" >
								</el-table-column>
								<el-table-column label="平均热度" sortable prop="hotData" >
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
				<el-row :gutter="20" v-if="(selected.name!='')" >
					<el-col :span="24">
						<el-card shadow="hover" style="height:630px;">
							<div slot="header" class="clearfix">
								<span>参与话题用户及微博</span>
							</div>
							<el-col :span="8" >
								<el-table  :data="topicUserTable" v-loading="(topicUserTable.length==0)">
									<el-table-column label="昵称" width="110px" prop="nickName" :show-overflow-tooltip="true">
									</el-table-column>
									<el-table-column label="微博数"  prop="num" >
									</el-table-column>
									<el-table-column label="操作">
										<template slot-scope="scope">
											<el-button size="mini" type="primary" @click="handleUserDetal(scope.row)">详细</el-button>
										</template>
									</el-table-column>
								</el-table>	
							</el-col>
							<el-col :span="16">
								<el-table  :data="topicUserWeiboTable" v-loading="(topicUserWeiboTable.length==0)" >
									<el-table-column label="微博" prop="content" :show-overflow-tooltip="true" width="400px">
									</el-table-column>
									<el-table-column label="点赞" sortable prop="likeNum" >
									</el-table-column>
									<el-table-column label="转发" prop="repostNum" >
									</el-table-column>
									<el-table-column label="回复" prop="commentNum" >
									</el-table-column>
								</el-table>	
							</el-col>
						</el-card>
					</el-col>
				</el-row>
				<el-row :gutter="20" v-if="(selected.name!='')">
					<el-col :span="24">
						<el-card shadow="hover" style="height:360px;">
							<div slot="header" class="clearfix">
								<span>热度趋势及预测</span>
							</div>
							<ve-line :data="topicHotlineDate" :data-zoom="dataZoom" height="280px" v-loading="(topicHotlineDate.rows.length==0)" :toolbox="toolbox" :colors="colors"></ve-line>
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
				topicData: [],
				topicUserWeiboTable:[],
				topicUserTable:[],
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
				topicHotlineDate: {
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
			topicTableData: function() {
				if (this.topicData == []) {
					return []
				} else {
					var data = [];
					var size = (this.topicData.length > 10) ? 10 : this.topicData.length;
					for (var i = 0; i < size; i++) {
						var arr = {
							order: i + 1,
							name: this.topicData[i][0],
							counts: this.topicData[i][1],
							hotData: this.topicData[i][2]
						}
						data.push(arr)
					}
					return data
				}
			},

		},
		created: function() {

			this.handleGetTopicHotData();
		},
		activated: function() {

		},
		deactivated: function() {

		},
		methods: {
			handleSerch: function() {
				this.handleGetTopicHotData()
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
			handleGetTopicHotData: function() {
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
					url: getTopicOderDataApi,
					data: obj
				}).then(function(res) {
					_this.topicData = res.data.data;
					_this.selected.name=res.data.data[0][0]
					_this.tableLoading = false;
					_this.handlegetTopicUser();
					_this.handleGetopicHotlineDate();
				}).catch(function() {
					console.log("请求失败");
				});
			},
			handleDetal: function(row) {
				this.selected.id = row.id;
				this.selected.name = row.name;
				this.topicUserTable=[];
				this.handlegetTopicUser();
				this.handleGetopicHotlineDate();
			},
			handleUserDetal:function(row){
				this.topicUserWeiboTable=[];
				this.handleGetTopicUserWeiboInfo(row.userId);
			},
			handlegetTopicUser: function() {
				var _this = this;
				var obj = {
					name:this.selected.name,
					startTime: this.startTime.substring(0, 10),
					endTime: this.endTime.substring(0, 10)
				};
				this.$axios({
					method: 'post',
					url: getTopicUserApi,
					data: obj
				}).then(function(res) {
					var result = res.data.data;
					_this.topicUserTable=[];
					for(var i=0;i<result.length;i++){
						var arr= {
							userId:result[i][0],
							nickName:result[i][1],
							num:result[i][3]
						}
						_this.topicUserTable.push(arr);
					}
					_this.topicUserWeiboTable=[];
					_this.handleGetTopicUserWeiboInfo(_this.topicUserTable[0].userId);
				}).catch(function() {
					console.log("请求失败");
				});
			},
			handleGetTopicUserWeiboInfo:function(userId){
				var _this = this;
				var obj = {
					topic:this.selected.name,
					startTime: this.startTime.substring(0, 10),
					endTime: this.endTime.substring(0, 10),
					userId:userId
				};
				this.$axios({
					method: 'post',
					url: getTopicUserWeiboInfoApi,
					data: obj
				}).then(function(res) {
					_this.topicUserWeiboTable=[]
					var result = res.data.data;
					_this.topicUserWeiboTable=result;
				}).catch(function() {
					console.log("请求失败");
				});
			},
			handleGetopicHotlineDate: function() {
				this.topicHotlineDate.rows=[];
				var _this = this;
				var obj = {
					topic: this.selected.name,
					startTime: this.startTime.substring(0, 10),
					endTime: this.endTime.substring(0, 10)
				};
				this.$axios({
					method: 'post',
					url:getTopicHotlineDateApi,
					data: obj
				}).then(function(res) {
					_this.topicHotlineDate.rows = res.data.data;

				}).catch(function() {
					console.log("请求失败");
				});
			},
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
