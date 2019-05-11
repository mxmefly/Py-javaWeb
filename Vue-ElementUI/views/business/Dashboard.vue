<template>
	<div>
		<el-row :gutter="20">
			<el-col :span="24">
				<el-row :gutter="20" class="mgb20">
					<el-col :span="4">
						<el-card shadow="hover" :body-style="{padding: '0px'}">
							<div class="grid-content grid-con-1">
								<i class="el-icon-lx-people grid-con-icon"></i>

								<div class="grid-cont-right">
									<div class="grid-num">{{showData.weiboUserNum}}</div>
									<div>抓取用户数</div>
								</div>
							</div>
						</el-card>
					</el-col>
					<el-col :span="4">
						<el-card shadow="hover" :body-style="{padding: '0px'}">
							<div class="grid-content grid-con-2">
								<i class="el-icon-lx-notice grid-con-icon"></i>

								<div class="grid-cont-right">
									<div class="grid-num">{{showData.weiboInfoNum}}</div>
									<div>抓取微博数</div>
								</div>
							</div>
						</el-card>
					</el-col>
					<el-col :span="4">
						<el-card shadow="hover" :body-style="{padding: '0px'}">
							<div class="grid-content grid-con-3">
								<i class="el-icon-lx-goods grid-con-icon"></i>

								<div class="grid-cont-right">
									<div class="grid-num">{{showData.weiboCommentNum}}</div>
									<div>抓取评论数</div>
								</div>
							</div>
						</el-card>
					</el-col>
					<el-col :span="4">
						<el-card shadow="hover" :body-style="{padding: '0px'}">
							<div class="grid-content grid-con-1">
								<i class="el-icon-lx-people grid-con-icon"></i>

								<div class="grid-cont-right">
									<div class="grid-num">{{showData.weiboBaseDataNum}}</div>
									<div>数据处理量</div>
								</div>
							</div>
						</el-card>
					</el-col>
					<el-col :span="4">
						<el-card shadow="hover" :body-style="{padding: '0px'}">
							<div class="grid-content grid-con-2">
								<i class="el-icon-lx-notice grid-con-icon"></i>

								<div class="grid-cont-right">
									<div class="grid-num">{{weiboToProcessNum}}</div>
									<div>待处理数据量</div>
								</div>
							</div>
						</el-card>
					</el-col>
					<el-col :span="4">
						<el-card shadow="hover" :body-style="{padding: '0px'}">
							<div class="grid-content grid-con-3">
								<i class="el-icon-lx-goods grid-con-icon"></i>

								<div class="grid-cont-right">
									<div class="grid-num">{{showData.touristNum}}</div>
									<div>访客数量</div>
								</div>
							</div>
						</el-card>
					</el-col>
				</el-row>
				<el-row :gutter="20" class="mgb20">
					<el-col :span="8">
						<el-card shadow="hover" style="height:360px;">
							<div slot="header" class="clearfix">
								<span>相关进度/使用率</span>
							</div>
							<div class="mgb15">
								微博处理
								<el-progress :percentage="weiboInfoProgressData" color="#42b983"></el-progress>
							</div>
							<div class="mgb15">
								评论处理
								<el-progress :percentage="weiboCommentProgressData" color="#f1e05a"></el-progress>
							</div>
							<div class="mgb15">
								账号池利用率
								<el-progress :percentage="showData.accountPro"></el-progress>
							</div>
							<div class="mgb15">
								服务器负载
								<el-progress :percentage="cpuUsed" color="#f56c6c"></el-progress>
							</div>
						</el-card>
					</el-col>
					<el-col :span="16">
						<el-card shadow="hover" style="height:360px;">
							<div slot="header" class="clearfix">
								<span>抓取用户日图（近10天）</span>
							</div>
							<ve-line :data="weiboUserlineDate" height="280px" :toolbox="toolbox" ></ve-line>
						</el-card>
					</el-col>
				</el-row>
				<el-row :gutter="20" class="mgb20">
					<el-col :span="24">
						<el-card shadow="hover" style="height:400px;">
							<div slot="header" class="clearfix">
								<span>抓取微博日图（近10天）</span>
							</div>
							<ve-line v-loading="loading1" :data="weiboLineData" height="320px" :toolbox="toolbox"></ve-line>
						</el-card>
					</el-col>
				</el-row>
				<el-row :gutter="20" class="mgb20">
					<el-col :span="24">
						<el-card shadow="hover" style="height:400px;">
							<div slot="header" class="clearfix">
								<span>抓取评论日图（近10天）</span>
							</div>
							<ve-line v-loading="loading2" :data="weiboCommentLineData" height="320px" :toolbox="toolbox"></ve-line>
						</el-card>
					</el-col>
				</el-row>


			</el-col>
		</el-row>
	</div>
</template>

<script>
	module.exports = {
		name: 'dashboard',
		data: function() {
			return {
				name: localStorage.getItem('ms_username'),
				chartSetting:{
					labelMap:{
						
					}
				},
				weiboUserlineDate: {
					columns: ['时间','爬取用户数'],
					rows: []
				},
				weiboLineData:{
					columns: ['时间','爬取微博数'],
					rows: []
				},
				weiboCommentLineData:{
					columns: ['时间','爬取评论数'],
					rows: []
				},
				showData: {
					touristNum: 0,
					weiboBaseDataNum: 0,
					weiboCommentNum: 0,
					weiboInfoNum: 0,
					weiboUserNum: 0,
					weiboInfoTodoNum: 0,
					weiboCommentTodoNum: 0,
					accountPro: 0
				},
				cpuUsed: 0,
				loading1: true,
				loading2: true,
				toolbox: {
					feature: {
						saveAsImage: {}
					}
				}
			}
		},
		computed: {
			role: function() {
				return this.name === 'admin' ? '超级管理员' : '普通用户';
			},
			weiboToProcessNum: function() {
				return this.showData.weiboCommentTodoNum + this.showData.weiboInfoTodoNum;
			},
			weiboInfoProgressData: function() {
				if (this.showData.weiboInfoNum == 0) {
					return 100;
				}
				return Math.round((this.showData.weiboInfoNum - this.showData.weiboInfoTodoNum) / this.showData.weiboInfoNum *
					100)
			},
			weiboCommentProgressData: function() {
				if (this.showData.weiboCommentNum == 0) {
					return 100;
				}
				return Math.round((this.showData.weiboCommentNum - this.showData.weiboCommentTodoNum) / this.showData.weiboCommentNum *
					100)
			}
		},
		created: function() {
			this.handleGetDataSize()
			setInterval(this.handleGetCpuUsd, 2000)
			this.handleGetUserlineDate()
			this.handleGetWeiboLineData()
			this.handleGetWeiboCommentLineData();
		},
		activated: function() {

		},
		deactivated: function() {

		},
		methods: {
			handleGetDataSize: function() {
				var _this = this;
				const loading = this.$loading({
					lock: true,
					text: '数据量过大，请耐心等待...',
					spinner: 'el-icon-loading',
					background: 'rgba(0, 0, 0, 0.7)'
				});
				var obj = {};
				this.$axios({
					method: 'post',
					url: getDataSizeApi,
					data: obj
				}).then(function(res) {
					loading.close()
					_this.showData = res.data.data;
				}).catch(function() {
					console.log("请求失败");
				});
			},
			handleGetCpuUsd: function() {
				var _this = this;
				var obj = {};
				this.$axios({
					method: 'post',
					url: getCpuUsdApi,
					data: obj
				}).then(function(res) {
					var s = res.data.cpu
					s = Math.round(s * 100)
					if (s < 100 && s > 0) {
						_this.cpuUsed = s
					}

				}).catch(function() {
					console.log("请求失败");
				});
			},
			handleGetUserlineDate: function() {
				var _this = this;
				var obj = {};
				this.$axios({
					method: 'post',
					url: getUserlineDateApi,
					data: obj
				}).then(function(res) {
					_this.weiboUserlineDate.rows=res.data.data;
				}).catch(function() {
					console.log("请求失败");
				});
			},
			handleGetWeiboLineData: function() {
				var _this = this;
				var obj = {};
				this.$axios({
					method: 'post',
					url: getWeiboLineDataApi,
					data: obj
				}).then(function(res) {
					//console.log(res)
					_this.loading1=false;
					_this.weiboLineData.rows=res.data.data;
				}).catch(function() {
					console.log("请求失败");
				});
			},
			handleGetWeiboCommentLineData: function() {
				var _this = this;
				var obj = {};
				this.$axios({
					method: 'post',
					url: getWeiboCommentLineDataApi,
					data: obj
				}).then(function(res) {
					_this.loading2=false;
					_this.weiboCommentLineData.rows=res.data.data;
				}).catch(function() {
					console.log("请求失败");
				});
			}
		}
	}
</script>


<style scoped>
	.el-row {
		margin-bottom: 20px;
	}

	.grid-content {
		display: flex;
		align-items: center;
		height: 80px;
	}

	.grid-cont-right {
		flex: 1;
		text-align: center;
		font-size: 14px;
		color: #999;
	}

	.grid-num {
		font-size: 25px;
		font-weight: bold;
	}

	.grid-con-icon {
		font-size: 50px;
		width: 15px;
		height: 100px;
		text-align: center;
		line-height: 100px;
		color: #fff;
	}

	.grid-con-1 .grid-con-icon {
		background: rgb(45, 140, 240);
	}

	.grid-con-1 .grid-num {
		color: rgb(45, 140, 240);
	}

	.grid-con-2 .grid-con-icon {
		background: rgb(100, 213, 114);
	}

	.grid-con-2 .grid-num {
		color: rgb(45, 140, 240);
	}

	.grid-con-3 .grid-con-icon {
		background: rgb(242, 94, 67);
	}

	.grid-con-3 .grid-num {
		color: rgb(242, 94, 67);
	}


	.mgb20 {
		margin-bottom: 20px;
	}

	.mgb15 {
		margin-bottom: 15px;
	}

</style>
