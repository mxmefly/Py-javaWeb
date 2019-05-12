<template>
	<div>
		<el-row :gutter="20">
			<el-col :span="24">
				<el-row :gutter="20" class="mgb20">
					<el-col :span="14">
						<el-card shadow="hover" style="height: 740px;">
							<div slot="header" class="clearfix">
								<span>表情热度排行（No.10）</span>
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
							<el-table :data="HotWordData" v-loading="(HotWordData.length==0)" :row-class-name="tableRowClassName">
								<el-table-column type="index" >
								</el-table-column>
								<el-table-column label="表情"  prop="word" :formatter="formatter1" >
								</el-table-column>
								<el-table-column label="数量" prop="counts">
								</el-table-column>
								<el-table-column label="操作" width="200px">
									<template slot-scope="scope">
										<el-button size="mini" type="primary" @click="handleDetal(scope.row)">相关用户</el-button>
									</template>
								</el-table-column>
							</el-table>
						</el-card>
					</el-col>
					<el-col :span="10">
						<el-card shadow="hover"  style="height: 75px;margin-bottom: 5px;">
						<div class="moudelTitle">
							    数据处理量有限，因此目前还不具备可靠的分析性
							<br><span>微博表情还未找到合适的表情icon素材包暂用文字代替</span>
						</div>
						</el-card>	
					</el-col>
					<el-col :span="10">
						<el-card shadow="hover" style="height: 660px;" v-loading="(wordUserData.length==0)">
							<div slot="header" class="clearfix">
								<span>相关用户（[{{selected.name}}]）</span>
							</div>
							<el-table :data="wordUserData"  >
								<el-table-column type="index" >
								</el-table-column>
								<el-table-column label="昵称" :show-overflow-tooltip="true" :formatter="formatter" prop="nickName" >
								</el-table-column>
								<el-table-column label="次数" prop="counts">
								</el-table-column>
								<el-table-column label="操作">
									<template slot-scope="scope">
										<el-button size="mini" type="warning" @click="handleGoHome(scope.row)">主页</el-button>
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
								<span>热度趋势及预测（[{{selected.name}}]）</span>
							</div>
							<ve-line :data="topicHotlineDate" :data-zoom="dataZoom" height="280px" v-loading="(topicHotlineDate.rows.length==0)"
							 :toolbox="toolbox" :colors="colors"></ve-line>
						</el-card>
					</el-col>
				</el-row>
			</el-col>
		</el-row>
	</div>
</template>

<script>
	module.exports = {
		name: 'HotWord',
		data: function() {
			return {
				HotWordData: [],
				wordUserData:[],
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
				tableLoading: false,
				selected: {
					id: "",
					name: ""
				},
				topicHotlineDate: {
					columns: ['时间', '热度', '预测'],
					rows: []
				},
				colors: ['#2f4554', '#c23531', ],
				dataZoom: [{
					type: 'slider',
				}],
				toolbox: {
					feature: {
						saveAsImage: {}
					}
				},
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
		},
		created: function() {
			this.handleGetHotWordData();
			
		},
		activated: function() {

		},
		deactivated: function() {

		},
		methods: {
			handleSerch: function() {
				this.handleGetHotWordData()
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
			handleGetHotWordData: function() {
				var _this = this;
				this.tableLoading = true;
				if (this.dateRange == "") {
					this.handleSetDefaltTime();
				}
				var obj = {
					type:'expression',
					startTime: this.startTime.substring(0, 10),
					endTime: this.endTime.substring(0, 10)
				};
				this.$axios({
					method: 'post',
					url: BASE_API + '/getHotBaseData',
					data: obj
				}).then(function(res) {
					var result = res.data.data;
					for(var i=0;i<result.length;i++){
						var arr = {
							word:result[i][0],
							counts:result[i][1]
						}
						_this.HotWordData.push(arr);
					}
					_this.selected.name = result[0][0]
					_this.tableLoading = false;
					_this.handleGetWordUserData();
					_this.handleGetWordHotlineDate();
					
				}).catch(function() {
					console.log("请求失败");
				});
			},
			handleDetal: function(row) {
				this.selected.id = row.id;
				this.selected.name = row.word;
				this.wordUserData=[];
				this.handleGetWordUserData();
				this.topicHotlineDate.rows=[];
				this.handleGetWordHotlineDate();
			},
			handleGetWordUserData:function(){
				var _this = this;
				var obj = {
					type:'expression',
					name: this.selected.name,
					startTime: this.startTime.substring(0, 10),
					endTime: this.endTime.substring(0, 10)
				};
				this.$axios({
					method: 'post',
					url: BASE_API + '/getBaseDataUser',
					data: obj
				}).then(function(res) {
					var result = res.data.data;
					for(var i=0;i<result.length;i++){
						var arr = {
							id:result[i][0],
							nickName:result[i][1],
							counts:result[i][2]
						}
						_this.wordUserData.push(arr);
					}
				
				}).catch(function() {
					console.log("请求失败");
				});
			},
			handleGetWordHotlineDate: function() {
				this.topicHotlineDate.rows = [];
				var _this = this;
				var obj = {
					name: this.selected.name,
					type:'expression',
					startTime: this.startTime.substring(0, 10),
					endTime: this.endTime.substring(0, 10)
				};
				this.$axios({
					method: 'post',
					url: BASE_API + '/getWordHotLine',
					data: obj
				}).then(function(res) {
					_this.topicHotlineDate.rows = res.data.data;

				}).catch(function() {
					console.log("请求失败");
				});
			},
			handleGoHome: function(row) {
				window.open("https://weibo.com/u/" + row.id);
			},
			formatter: function (row, column) {
			    return (row.nickName==null)?"暂未爬取该用户":row.nickName;
			},
			formatter1: function (row, column) {
			    return '['+row.word+']';
			},
			
		}
	}
</script>


<style scoped>
	.el-row {
		margin-bottom: 15px;
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
