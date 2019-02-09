<template>
	<div class="">
		<div class="crumbs">
			<el-breadcrumb separator="/">
				<el-breadcrumb-item><i class="el-icon-lx-copy"></i> 系统消息</el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<div class="container">
			<el-tabs v-model="message">
				<el-tab-pane :label="`未读消息(${unread.length})`" name="first">
					<el-table :data="unread" :show-header="false" style="width: 100%">
						<el-table-column>
							<template slot-scope="scope">
								<span class="message-title">{{scope.row.message}}</span>
							</template>
						</el-table-column>
						<el-table-column width="240">
							<template slot-scope="scope">
								<span>{{ getLocalTime(scope.row.messageDate) }}</span>
							</template>
						</el-table-column>
						<el-table-column width="120">
							<template slot-scope="scope">
								<el-button size="small" @click="handleRead(scope.$index)" size="small">标为已读</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="handle-row">
						<el-button type="primary" size="small" @click="handleReadAll" >全部标为已读</el-button>
					</div>
				</el-tab-pane>
				<el-tab-pane :label="`已读消息(${read.length})`" name="second">
					<template v-if="message === 'second'">
						<el-table :data="read" :show-header="false" style="width: 100%">
							<el-table-column>
								<template slot-scope="scope">
									<span class="message-title">{{scope.row.message}}</span>
								</template>
							</el-table-column>
							<el-table-column width="240">
								<template slot-scope="scope">
									<span>{{ getLocalTime(scope.row.messageDate) }}</span>
								</template>
							</el-table-column>
							<el-table-column width="120">
								<template slot-scope="scope">
									<el-button type="danger" @click="handleDel(scope.$index)" size="small">删除
									</el-button>
								</template>
							</el-table-column>
						</el-table>
						<div class="handle-row">
							<el-button type="danger" size="small" @click="handleDelMsgAll">删除全部</el-button>
						</div>
					</template>
				</el-tab-pane>
			</el-tabs>
		</div>
	</div>
</template>

<script>
	module.exports = {
		name: 'SysMsg',
		data: function() {
			return {
				message: 'first',
				showHeader: false,
				unread: [],
				read: [],
				recycle: []
			}
		},
		computed: {
			unreadNum: function() {
				return this.unread.length;
			},
		},
		methods: {
			handleGetMsgData: function() {
				var _this = this;
				var obj = {};
				this.$axios({
					method: 'post',
					url: SysMsg_Apis.getMsgData,
					data: obj
				}).then(function(res) {
					var result = res.data.data;
					_this.unread = result.unread;
					_this.read = result.read;
					_this.recycle = result.recycle;
				}).catch(function() {
					console.log("请求失败");
				});
			},
			handleRead: function(index) {
				var item = this.unread.splice(index, 1);
				var _this = this;
				var obj = {
					id: item[0].id,
					state: 1
				};
				this.$axios({
					method: 'post',
					url: SysMsg_Apis.updateMsgState,
					data: obj
				}).then(function(res) {
					if (res.data.success) {
						_this.$store.dispatch('GetInfo');
						_this.handleGetMsgData();
					} else {
						_this.$message({
							message: '请求失败，请刷新重试',
							type: 'warning'
						});
					}
				}).catch(function() {
					console.log("请求失败");
				});
				
			},
			handleReadAll:function(){
				var _this = this;
				var obj = {
					state: 1,
					oldState:0
				};
				this.$axios({
					method: 'post',
					url: SysMsg_Apis.updateStateAll,
					data: obj
				}).then(function(res) {
					if (res.data.data>0) {
						_this.$store.dispatch('GetInfo');
						_this.handleGetMsgData();
					} else {
						_this.$message({
							message: '请求失败，请刷新重试',
							type: 'warning'
						});
					}
				}).catch(function() {
					console.log("请求失败");
				});		
			},
			handleDel: function(index) {
				var item = this.read.splice(index, 1);
				var _this = this;
				var obj = {
					id:item[0].id
				};
				this.$axios({
					method: 'post',
					url: SysMsg_Apis.delMsgById,
					data: obj
				}).then(function(res) {
					if (res.data.success) {
						_this.handleGetMsgData();
					} else {
						_this.$message({
							message: '请求失败，请刷新重试',
							type: 'warning'
						});
					}
				}).catch(function() {
					console.log("请求失败");
				});
			},
			handleDelMsgAll:function(){
				var _this = this;
				var obj = {
				};
				this.$axios({
					method: 'post',
					url: SysMsg_Apis.delMsgByAll,
					data: obj
				}).then(function(res) {
					if (res.data.success) {
						_this.$message({
							message: '共删除了'+res.data.data+"条数据",
							type: 'success'
						});
						_this.handleGetMsgData();
					} else {
						_this.$message({
							message: '请求失败，请刷新重试',
							type: 'warning'
						});
					}
				}).catch(function() {
					console.log("请求失败");
				});
			},
			getLocalTime: function(timestamp) {
				var d = new Date(timestamp);
				var date = (d.getFullYear()) + "-" +
					(d.getMonth() + 1) + "-" +
					(d.getDate()) + " " +
					(d.getHours() < 10 ? ("0" + d.getHours()) : (d.getHours())) + ":" +
					(d.getMinutes() < 10 ? ("0" + d.getMinutes()) : (d.getMinutes())) + ":" +
					(d.getSeconds() < 10 ? ("0" + d.getSeconds()) : (d.getSeconds()));
				return date;
			}
		},
		mounted: function() {
			this.handleGetMsgData();
		}
	}
</script>

<style>
	.message-title {
		cursor: pointer;
	}

	.handle-row {
		margin-top: 30px;
	}
</style>
