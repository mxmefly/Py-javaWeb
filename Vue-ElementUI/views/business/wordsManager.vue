<template>
	<div>
		<el-row :gutter="20">
			<el-col :span="12">
				<el-card shadow="hover" class="mgb20" style="height: 320px;">
					<div slot="header" class="clearfix">
						<span>上传词库</span>
					</div>
					<el-form ref="form" label-width="70px">
						<el-form-item label="词库路径">
							<el-input v-model="filePath"></el-input>
						</el-form-item>
						<el-form-item label="词库类型">
							<el-input v-model="wordType"></el-input>
						</el-form-item>
						<el-alert title="请输入正确的文件路径(绝对路径),此操作或耗费巨长时间" type="warning">
						</el-alert>
						<el-form-item>
							<el-button type="primary" @click="onSubmit" size="small">上传</el-button>
							<el-button size="small" @click="refresh">重置</el-button>
						</el-form-item>
					</el-form>
				</el-card>
			</el-col>
			<el-col :span="12">
				<el-card shadow="hover" class="mgb20" style="height: 320px;">
					<div slot="header" class="clearfix">
						<span>导出词库</span>
					</div>
					<div style="text-align: center; margin-bottom: 15px;margin-top: 65px;">
						<el-button type="primary" plain style="width: 140px; height: 85px;" disabled >导出词库</el-button>
					</div>
				</el-card>
			</el-col>
		</el-row>
		<el-row>
			<el-col :span="24">
				<el-card>
					<div slot="header" class="clearfix">
						<span>BaseWord词库管理</span>
					</div>
					<el-table :data="baseWordTableData" border class="table" ref="multipleTable" size="small">
						<el-table-column prop="word" label="字词" >
						</el-table-column>
						<el-table-column prop="counts" sortable label="次数" >
						</el-table-column>
						<el-table-column prop="type" sortable label="类型" >
						</el-table-column>
						<el-table-column prop="isShow" label="是否显示" :formatter="formatter">
						</el-table-column>
						<el-table-column label="操作" width="180" align="center">
							<template slot-scope="scope">
								<el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)" size="small">{{(scope.row.isShow==1)?'不再显示':'恢复显示'}}
								</el-button>
								<el-button type="text" icon="el-icon-delete" class="red" @click="handleDelete(scope.$index, scope.row)" size="small">删除
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="pagination">
						<el-pagination background @current-change="handleCurrentChange" layout="prev, pager, next" :page-size='10'
						 :total="total">
						</el-pagination>
					</div>
				</el-card>
			</el-col :span="24">
				<el-card>
					<div slot="header" class="clearfix">
						<span>NewWord词库管理</span>
					</div>
					<el-table :data="newWordTableData" border class="table" ref="multipleTable" size="small">
						<el-table-column prop="word" label="字词" >
						</el-table-column>
						<el-table-column prop="count" sortable label="次数" >
						</el-table-column>
						<el-table-column label="操作" width="180" align="center">
							<template slot-scope="scope">
								<el-button type="text" icon="el-icon-ex-send" @click="handleImpot(scope.$index, scope.row)" size="small">导入词库
								</el-button>
								<el-button type="text" icon="el-icon-delete" class="red" @click="handleDelete2(scope.$index, scope.row)" size="small">删除
								</el-button>
							</template>
						</el-table-column>
					</el-table>
					<div class="pagination">
						<el-pagination background @current-change="handleCurrentChange2" layout="prev, pager, next" :page-size='10'
						 :total="total2">
						</el-pagination>
					</div>
				</el-card>
			<el-col>
			</el-col>	
		</el-row>
	</div>
</template>

<script>
	module.exports = {
		name: 'wordManager',
		data: function() {
			return {
				filePath: "",
				wordType: "",
				baseWordTableData: [],
				newWordTableData:[],
				total:0,
				total2:0,
				cur_page: 1,
				cur_page2:1,
			}
		},
		methods: {
			onSubmit: function() {
				const loading = this.$loading({
					lock: true,
					text: 'Loading',
					spinner: 'el-icon-loading',
					background: 'rgba(0, 0, 0, 0.7)'
				});
				var _this = this;
				var obj = {
					filePath: this.filePath,
					wordType: this.wordType
				};
				this.$axios({
					method: 'post',
					url: wordsImport,
					data: obj
				}).then(function(res) {
					if (res.data.success) {
						_this.$message.success(res.data.data)
					} else {
						_this.$message.error(res.data.data)
					}
					loading.close()
				}).catch(function() {
					console.log("请求失败");
				});
			},
			refresh: function() {
				this.filePath = "";
				this.wordType = '';
			},
			handleCurrentChange: function (val) {
			    this.cur_page = val;
			    this.getBaseWordData();
			},
			// 获取 easy-mock 的模拟数据
			getBaseWordData: function () {
			    var _this = this;
			    var obj = {
					page: this.cur_page-1,
					size: 10
				};
			    this.$axios({
			        method: 'post',
			        url: BASE_API+'/getWordList',
			        data: obj
			    }).then(function (res) {
			        _this.baseWordTableData = res.data.data.content;
					_this.total=res.data.data.totalElements;	
			    }).catch(function () {
			        console.log("请求失败");
			    });
			},
			handleEdit: function (index, row) {
				var _this = this;
				var obj = {
					id:row.id
				};
				this.$axios({
				    method: 'post',
				    url: BASE_API+'/editWordShow',
				    data: obj
				}).then(function (res) {
					if (res.data.data) {
						_this.$message.success("修改成功")
						_this.getBaseWordData();
					} else {
						_this.$message.error("修改失败")
					}	
				}).catch(function () {
				    console.log("请求失败");
				});
			},
			handleDelete: function (index, row) {
				var _this = this;
				var obj = {
					id:row.id
				};
				this.$axios({
				    method: 'post',
				    url: BASE_API+'/deleteWordOne',
				    data: obj
				}).then(function (res) {		
					if (res.data.data) {
						_this.getBaseWordData();
						_this.$message.success("删除成功")	
					} else {
						_this.$message.error("删除失败")
					}
				}).catch(function () {
				    console.log("请求失败");
				});
			},
			getNewWordData: function () {
			    var _this = this;
			    var obj = {
					page: this.cur_page-1,
					size: 10
				};
			    this.$axios({
			        method: 'post',
			        url: BASE_API+'/getNewWordList',
			        data: obj
			    }).then(function (res) {
			        _this.newWordTableData = res.data.data.content;
					_this.total2=res.data.data.totalElements;	
			    }).catch(function () {
			        console.log("请求失败");
			    });
			},
			handleImpot:function(index, row){
				var _this = this;
				var obj = {
					id:row.id,
					word:row.word,
					count:row.count
				};
				this.$axios({
				    method: 'post',
				    url: BASE_API+'/newWordImport',
				    data: obj
				}).then(function (res) {		
					if (res.data.data) {
						_this.getNewWordData();
						_this.$message.success("导入成功")	
					} else {
						_this.$message.error("导入失败")
					}
				}).catch(function () {
				    console.log("请求失败");
				});
			},
			formatter: function (row, column) {
			    return (row.isShow==1)?'是':'否';
			},
			handleDelete2: function (index, row) {
				var _this = this;
				var obj = {
					id:row.id
				};
				this.$axios({
				    method: 'post',
				    url: BASE_API+'/deleteNewWordOne',
				    data: obj
				}).then(function (res) {		
					if (res.data.data) {
						_this.getNewWordData();
						_this.$message.success("删除成功")	
					} else {
						_this.$message.error("删除失败")
					}
				}).catch(function () {
				    console.log("请求失败");
				});
			},
			handleCurrentChange2: function (val) {
			    this.cur_page2 = val;
			    this.getNewWordData();
			},
		},
		computed: {
			
		},
		created: function () {
		    this.getBaseWordData();
			this.getNewWordData();
		},
	}
</script>

<style>
	.mgb20 {
		margin-bottom: 20px;
	}

	.el-form-item {
		margin-top: 15px !important;
		margin-bottom: 15px !important;
	}
</style>
