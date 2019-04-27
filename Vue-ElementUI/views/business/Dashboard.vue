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
									<div class="grid-num">{{showData.weiboToProcessNum}}</div>
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
						<el-card shadow="hover" style="height:320px;">
							
						</el-card>
					</el-col>
					<el-col :span="16">
						<el-card shadow="hover" style="height:320px;">
							
						</el-card>
					</el-col>
				</el-row>
				<el-card shadow="hover" style="height:403px;">
					<div slot="header" class="clearfix">
						<el-input placeholder="请输入待办事项" v-model="todoListItem">
							<template slot="prepend">待办事项</template>
							<template slot="append" style="align-content: center;">
								<el-button type="success" @click="handleAddTodoList">添加</el-button>
							</template>
						</el-input>

					</div>
					<el-table :data="todoList" :show-header="false" height="250" style="width: 100%;font-size:14px;">
						<el-table-column width="40">
							<template slot-scope="scope">
								<el-checkbox v-model="scope.row.status"></el-checkbox>
							</template>
						</el-table-column>
						<el-table-column>
							<template slot-scope="scope">
								<div class="todo-item" :class="{'todo-item-del': scope.row.status}">
									{{scope.row.title}}
								</div>
							</template>
						</el-table-column>
						<el-table-column width="60">
							<template slot-scope="scope">
								<el-button type="text" icon="el-icon-delete" size="small" @click="handleDelClick(scope.row)"></el-button>
							</template>
						</el-table-column>
					</el-table>
				</el-card>
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
				todoList: [],
				todoListItem: '',
				showData:{
					touristNum: 0,
					weiboBaseDataNum: 0,
					weiboCommentNum: 0,
					weiboInfoNum: 0,
					weiboToProcessNum: 0,
					weiboUserNum: 0,
				}
			}
		},
		computed: {
			role: function() {
				return this.name === 'admin' ? '超级管理员' : '普通用户';
			}
		},
		created: function() {
			this.handleGetDataSize()
		},
		activated: function() {

		},
		deactivated: function() {

		},
		methods: {
			handleDelClick: function(row) {
				if (row.status == true) {
					newTodoList = [];
					for (var i = 0; i < this.todoList.length; i++) {
						if (this.todoList[i].title != row.title) {
							newTodoList.push(this.todoList[i])
						}
					}
					this.todoList = newTodoList;
				}
			},
			handleAddTodoList: function() {
				var arr = {
					title: this.todoListItem,
					status: false
				}
				this.todoList.push(arr)
				this.todoListItem = ''
			},
			handleGetDataSize: function() {
				var _this=this;
				const loading = this.$loading({
					lock: true,
					text: '数据量过大，请耐心等待',
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
                    _this.showData=res.data.data;
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

	.user-info {
		display: flex;
		align-items: center;
		padding-bottom: 20px;
		border-bottom: 2px solid #ccc;
		margin-bottom: 20px;
	}

	.user-avator {
		width: 120px;
		height: 120px;
		border-radius: 50%;
	}

	.user-info-cont {
		padding-left: 50px;
		flex: 1;
		font-size: 14px;
		color: #999;
	}

	.user-info-cont div:first-child {
		font-size: 30px;
		color: #222;
	}

	.user-info-list {
		font-size: 14px;
		color: #999;
		line-height: 25px;
	}

	.user-info-list span {
		margin-left: 70px;
	}

	.mgb20 {
		margin-bottom: 20px;
	}

	.todo-item {
		font-size: 14px;
	}

	.todo-item-del {
		text-decoration: line-through;
		color: #999;
	}

	.schart {
		width: 100%;
		height: 300px;
	}
</style>
