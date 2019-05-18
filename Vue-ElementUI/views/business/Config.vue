<template>
	<div>
		<el-row :gutter="20">
			<el-col :span="24">
				<el-row :gutter="20" class="mgb20">
					<el-col :span="24">
						<el-card shadow="hover">
							<div slot="header" class="clearfix">
								<span>计划任务</span>
							</div>
							<el-col :span="24" class="mgb20">
								<div class="moudelTitle">
									当前计划<span>{{eventStatus?"已开启":"未开启"}}</span>，点击
									<el-button disabled>{{eventStatus?"关闭":"开启"}}</el-button>
								</div>
							</el-col>
							<el-col :span="24" class="mgb20">
								<el-table :data="eventTableData">
									</el-table-column>
									<el-table-column label="名称" prop="name">
									</el-table-column>
									<el-table-column label="次数" prop="value">
									</el-table-column>
									<el-table-column label="周期" prop="field">
									</el-table-column>
									<el-table-column label="描述" :show-overflow-tooltip="true" prop="comment">
									</el-table-column>
								</el-table>
							</el-col>
						</el-card>

					</el-col>
				</el-row>
				<el-row :gutter="20" class="mgb20">
					<el-col :span="24">
						<el-card>
							<div slot="header" class="clearfix">
								<span>过程执行日志</span>
							</div>
							<el-table :data="LogtableData">
								</el-table-column>
								<el-table-column type="index">
								</el-table-column>
								<el-table-column label="过程名" prop="name">
								</el-table-column>
								<el-table-column label="状态" prop="status" :formatter="formatter1">
								</el-table-column>
								<el-table-column label="时间" prop="date" :formatter="formatter2">
								</el-table-column>
							</el-table>
							<div class="pagination">
								<el-pagination background @current-change="handleCurrentChange" layout="prev, pager, next" :page-size='tableSize'
								 :total="total">
								</el-pagination>
							</div>
						</el-card>
					</el-col>
				</el-row>
				<el-row :gutter="20" class="mgb20">
					<el-col :span="24">
						<el-card>
							<div slot="header" class="clearfix">
								<span>爬虫深度配置</span>
							</div>
							<el-form ref="form" label-width="150px">
								<el-form-item label="最大微博页数">
									<el-input v-model="weibo" style="width: 400px;"></el-input>
								</el-form-item>
								<el-form-item label="最大评论页数">
									<el-input v-model="comment" style="width: 400px;"></el-input>
								</el-form-item>
								<el-form-item label="最早日期">
									<el-date-picker v-model="date" type="date" width="400px" placeholder="选择日期">
									</el-date-picker>
								</el-form-item>
								<el-form-item>
									<el-button type="primary" @click="handeleSaveConfig">保存</el-button>
								</el-form-item>
							</el-form>
						</el-card>
					</el-col>
				</el-row>


			</el-col>
		</el-row>
	</div>
</template>

<script>
	module.exports = {
		name: 'Config',
		data: function() {
			return {
				eventStatus: true,
				eventTableData: [],
				total: 0,
				tableSize: 5,
				LogtableData: [],
				cur_page: 1,
				weibo: 50,
				comment: 4,
				date: ""
			}
		},
		computed: {

		},
		created: function() {
			this.handleGetEventData();
			this.handleGetSpiderConfig();
			this.handleGetLogData();
		},
		activated: function() {

		},
		deactivated: function() {

		},
		methods: {
			handleGetEventData: function() {
				var _this = this;
				var obj = {};
				this.$axios({
					method: 'post',
					url: getEventConfigDateApi,
					data: obj
				}).then(function(res) {
					var result = res.data.data;
					_this.eventStatus = (result.eventStatus[0][1] == "ON")
					_this.eventTableData = [];
					for (var i = 0; i < result.eventsList.length; i++) {
						var arr = {
							name: result.eventsList[i][0],
							value: result.eventsList[i][1],
							field: result.eventsList[i][2],
							comment: result.eventsList[i][4],
						}
						_this.eventTableData.push(arr);
					}
				}).catch(function() {
					Console.log("请求失败");
				});
			},
			handleGetSpiderConfig: function() {
				var _this = this;
				var obj = {
				    name:"spider_config"
				};
				this.$axios({
					method: 'post',
					url: getConfigApi,
					data: obj
				}).then(function(res) {
					var result = res.data.data;
					_this.weibo = result[0].intVal1;
					_this.comment = result[0].intVal2;
					_this.date= new Date(Date.parse(result[0].strVal1));
				}).catch(function() {
					Console.log("请求失败");
				});
			},
			handleCurrentChange: function(val) {
				this.cur_page = val;
				this.handleGetLogData();
			},
			handleGetLogData: function() {
				var _this = this;
				var obj = {
					page: this.cur_page - 1,
					size: this.tableSize
				};
				this.$axios({
					method: 'post',
					url: getLogTableApi,
					data: obj
				}).then(function(res) {
					console.log(res);
					_this.LogtableData = res.data.data.content;
					_this.total = res.data.data.totalElements;
				}).catch(function() {
					console.log("请求失败");
				});
			},
			handeleSaveConfig: function() {
                 var _this = this;
                 var obj = {
					name:"spider_config",
                 	weibo:this.weibo,
					comment:this.comment,
					date:this.formatDate(this.date)
                 };
                 this.$axios({
                 	method: 'post',
                 	url: saveSpiderConfigApi,
                 	data: obj
                 }).then(function(res) {
                 	if (res.data.data) {
                 		_this.handleGetSpiderConfig();
                 		_this.$message.success("保存成功")	
                 	} else {
                 		_this.$message.error("保存失败")
                 	}
                 }).catch(function() {
                 	console.log("请求失败");
                 });
			},
			formatter1: function(row, column) {
				return (row.status == 1) ? "成功" : "失败"
			},
			formatter2: function(row, column) {
				return new Date(parseInt(row.date)).toLocaleString().replace(/:\d{1,2}$/, ' ');
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
				return y + '-' + m + '-' + d + ' 00:00:00';
			}

		}
	}
</script>


<style scoped>
	.el-row {
		margin-bottom: 20px;
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
</style>
