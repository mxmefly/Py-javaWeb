<template>
	<div>
		<el-row :gutter="20">
			<el-card shadow="hover" style="height:90px;">
				<div class="moudelTitle">
					<span>说明：</span>
					<br>本数据选取的参考样本容量为 <span>1000 </span>占总获取用户的 <span> {{Math.round(100000/allUserCounts)}}% 。</span>具有足够的参考性
					<br>但该项目起初只是抓取大V用户，后期抓取了大量用户，故粉丝数量分布目前参考性不大
				</div>
			</el-card>
		</el-row>	
		<el-row :gutter="20">
			<el-col :span="24">
				<el-row :gutter="20">
					<el-col :span="24">
						<el-card shadow="hover" style="height:450px;">
							<div slot="header" class="clearfix">
								<span>用户拥有粉丝数量分布</span>
							</div>
							<el-col :span="12">
								<el-table :data="fansNumData.rows" border :default-sort="{prop: 'num', order: 'descending'}">
									</el-table-column>
									<el-table-column label="量级" prop="name">
									</el-table-column>
									<el-table-column label="样本内数量" prop="num">
									</el-table-column>
									<el-table-column label="占比" prop="proportion">
									</el-table-column>
								</el-table>
							</el-col>
							<el-col :span="12">
								<ve-pie :data="fansNumData" :toolbox="toolbox"></ve-pie>
							</el-col>
						</el-card>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="12">
						<el-card shadow="hover" style="height:450px;">
							<div slot="header" class="clearfix">
								<span>年龄分布</span>
							</div>
							<ve-histogram :data="ageNumMap" :settings="chartSettings" :toolbox="toolbox"></ve-histogram>
						</el-card>
					</el-col>
					<el-col :span="12">
						<el-card shadow="hover" style="height:450px;">
							<div slot="header" class="clearfix">
								<span>性别分布</span>
							</div>
							<ve-pie :data="genderNumMap" :toolbox="toolbox"></ve-pie>
						</el-card>
					</el-col>
				</el-row>
				<el-row>
					<el-col :span="24">
						<el-card shadow="hover" style="height:500px;">
							<div slot="header" class="clearfix">
								<span>用户地区分布及比重</span>
							</div>
							
							<el-col :span="12">
								<ve-map :data="provinceNumMap" :settings="chartSettings" :toolbox="toolbox"></ve-map>
							</el-col>
							<el-col :span="12">
								<ve-pie :data="provinceNumMap" :settings="chartSettings" :toolbox="toolbox"></ve-pie>
							</el-col>
						</el-card>
					</el-col>
				</el-row>
				<el-row :gutter="20">
					<el-col :span="24">
						<el-card shadow="hover" style="height:550px;">
							<div slot="header" class="clearfix">
								<span>会员等级分布及比重</span>
							</div>
							<el-col :span="12">
								<el-table :data="vipLevelNumMap.rows" border >
									</el-table-column>
									<el-table-column label="量级" prop="name">
									</el-table-column>
									<el-table-column label="样本内数量" prop="num">
									</el-table-column>
									<el-table-column label="占比" prop="proportion">
									</el-table-column>
								</el-table>
							</el-col>
							<el-col :span="12">
								<ve-pie :data="vipLevelNumMap" :toolbox="toolbox"></ve-pie>
							</el-col>
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
				allUserCounts:1,
				toolbox: {
					feature: {
						saveAsImage: {}
					}
				},
				fansNumData: {
					columns: ['name', 'num'],
					rows: []
				},
				ageNumMap: {
					columns: ['name', 'num'],
					rows: []
				},
				chartSettings: {
					legendName: {
						'num': '数量'
					}
				},
				genderNumMap:{
					columns: ['name', 'num'],
					rows: []		
				},
				provinceNumMap:{
					columns: ['name', 'num'],
					rows: []		
				},
				vipLevelNumMap:{
					columns: ['name', 'num'],
					rows: []		
				},
					
			}
		},
		computed: {
			fansNumMapData: function() {

			}

		},
		created: function() {
			this.handleGetData()
		},
		activated: function() {

		},
		deactivated: function() {

		},
		methods: {
			handleGetData: function() {
				var _this = this;
				var obj = {};
				// obj = JSON.stringify(obj);
				this.$axios({
					method: 'post',
					url: BASE_API + '/allData/getUserInfo',
					data: obj
				}).then(function(res) {
					console.log("res", res);
					_this.fansNumData.rows = res.data.data.fansNumMap;
					_this.ageNumMap.rows = res.data.data.ageNumMap;
					_this.genderNumMap.rows=res.data.data.genderNumMap;
					_this.provinceNumMap.rows=res.data.data.provinceNumMap;
					_this.vipLevelNumMap.rows=res.data.data.vipLevelNumMap;
					_this.allUserCounts=res.data.data.allUserCounts;
				}).catch(function() {
					console.log("请求失败");
				});
			},
		}
	}
</script>


<style scoped>
	.el-row {
		margin-bottom: 20px;
	}

	.el-col {
		padding-left: 4px !important;
		padding-right: 4px !important;
		padding-top: 1px !important;
		padding-bottom: 1px !important;
	}
    .moudelTitle {
    	color: #1482F0
    }
    
    .moudelTitle span {
    	color: #E6A23C;
    	font-size: 18px;
    }
	.mgb20 {
		margin-bottom: 20px;
	}

	.mgb15 {
		margin-bottom: 15px;
	}
</style>
