<template>
	<div>
		<div class="crumbs">
			<el-breadcrumb separator="/">
				<el-breadcrumb-item><i class="el-icon-ex-check"></i>V-charts Demo</el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<el-row :gutter="20">
			<el-col :span="8">
				<el-card shadow="hover" class="mgb20" style="height:290px; overflow: auto;">
					<ve-line :data="lineChartData" height="250px"></ve-line>
				</el-card>
				<el-card shadow="hover" style="height:282px; overflow: auto;">
					<ve-pie :data="pieChartData" :settings="pieChartSettings" ></ve-pie>
				</el-card>
			</el-col>
			<el-col :span="16">
				<el-card shadow="hover" class="mgb20" style="height:50px;">
					<div >
					    引入V-charts UI库（Vue+E-charts） 
					    访问地址：<a href="https://v-charts.js.org" target="_blank">V-charts</a>
					</div>
				</el-card>
				<el-card shadow="hover" style="height:526px;">
					<ve-histogram :data="htChartData" :settings="htChartSettings" height="520px"></ve-histogram>
				</el-card>
			</el-col>
		</el-row>

	</div>
</template>

<script>
	module.exports = {
		name: 'BaseEcharts',
		data: function() {
			return {
				htChartSettings: {
					axisSite: {
						right: ['下单率']
					},
					yAxisType: ['KMB', 'percent'],
					yAxisName: ['数值', '比率']
				},
				pieChartSettings: {
					level: [
						['1/1', '1/2', '1/3'],
						['1/4', '1/5']
					]
				},
				lineChartData: {
					columns: ['日期', '访问用户', '下单用户', '下单率'],
					rows: [{
							'日期': '1/1',
							'访问用户': 1393,
							'下单用户': 1093,
							'下单率': 0.32
						},
						{
							'日期': '1/2',
							'访问用户': 2530,
							'下单用户': 3230,
							'下单率': 0.26
						},
						{
							'日期': '1/3',
							'访问用户': 2923,
							'下单用户': 2623,
							'下单率': 0.76
						},
						{
							'日期': '1/4',
							'访问用户': 1723,
							'下单用户': 1423,
							'下单率': 0.49
						},
						{
							'日期': '1/5',
							'访问用户': 3292,
							'下单用户': 3492,
							'下单率': 0.323
						}
					]
				},
				htChartData: {
					columns: ['日期', '访问用户', '下单用户', '下单率'],
					rows: [{
							'日期': '1/1',
							'访问用户': 1393,
							'下单用户': 1093,
							'下单率': 0.32
						},
						{
							'日期': '1/2',
							'访问用户': 3530,
							'下单用户': 3230,
							'下单率': 0.26
						},
						{
							'日期': '1/3',
							'访问用户': 2923,
							'下单用户': 2623,
							'下单率': 0.76
						},
						{
							'日期': '1/4',
							'访问用户': 1723,
							'下单用户': 1423,
							'下单率': 0.49
						},
						{
							'日期': '1/5',
							'访问用户': 3792,
							'下单用户': 3492,
							'下单率': 0.323
						},
						{
							'日期': '1/6',
							'访问用户': 4593,
							'下单用户': 4293,
							'下单率': 0.78
						}
					]
				},
				pieChartData: {
					columns: ['日期', '访问用户'],
					rows: [{
							'日期': '1/1',
							'访问用户': 1393
						},
						{
							'日期': '1/2',
							'访问用户': 3530
						},
						{
							'日期': '1/3',
							'访问用户': 2923
						},
						{
							'日期': '1/4',
							'访问用户': 1723
						},
						{
							'日期': '1/5',
							'访问用户': 3792
						},
						{
							'日期': '1/6',
							'访问用户': 4593
						}
					]
				}

			}
		},
		methods: {

		},
		computed: {

		}
	}
</script>


<style>
	.el-row {
		margin-bottom: 20px;
	}

	.mgb20 {
		margin-bottom: 20px;
	}


	.schart {
		width: 100%;
		height: 300px;
	}
</style>
