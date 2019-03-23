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

				</el-card>
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
				wordType: ""
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
					if(res.data.success){
						_this.$message.success(res.data.data)
					}else{
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
			}
		},
		computed: {}
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
