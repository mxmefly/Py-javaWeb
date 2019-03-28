<template>
	<div>
		<div class="crumbs">
			<el-breadcrumb separator="/">
				<el-breadcrumb-item>中文数据处理</el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<el-row :gutter="20">
			<el-col :span="24">
				<el-card shadow="hover" class="mgb20" style="height: 240px">
					<div slot="header">
						<span>文字处理</span>
						<div v-if="textarea.length!=0" style="float: right;">
							<el-button type="primary" icon="el-icon-edit" size="mini" @click="handleAnalysis">分析</el-button>
							<el-button type="success" icon="el-icon-check" size="mini" @click="handleTranslation">翻译</el-button>
						</div>
					</div>
					<el-input type="textarea" :rows="6" placeholder="请键入文本" style="overflow-y:auto" v-model="textarea">
					</el-input>
				</el-card>
			</el-col>
		</el-row>
		<el-row :gutter="20">
			<el-col :span="24" v-if="showTranlationCard">
				<el-card shadow="hover" class="mgb20" style="height: 360px">
					<div slot="header">
						<span>翻译结果<a href="http://ai.youdao.com/gw.s" target="_blank">(基于有道云提供的api)</a></span>
					</div>
					<el-input type="textarea" :rows="12" readonly style="overflow-y:auto" v-model="enText">
					</el-input>
				</el-card>

				<el-card v-if="showAnalysisCard" shadow="hover" class=" " style="height: 300px">

				</el-card>
			</el-col>
			<el-col :span="24" v-if="showAnalysisCard">
				<el-card v-if="showAnalysisCard" shadow="hover" class="mgb20" style="height: 300px;">
					<div slot="header">
						<span>分词（点击可快速复制和查看拼音英译）</span>
						<el-switch v-model="isMultSelect" active-text="多词" inactive-text="单词">
						</el-switch>
						<div v-if="isShowButton" style="float: right;">
							<el-button type="primary" size="mini" @click="handleSelectedCopy">复制</el-button>
							<el-button type="success" size="mini" @click="handleSelectedTranslation">翻译</el-button>
							<el-button type="warning" size="mini" @click="handleSelectedSerch">搜索</el-button>
						</div>
					</div>
					<div v-if="showAnalysisCard" style="height: 222px;overflow-y: auto;">
						<el-tag :key="index" v-for="(word,index) in listWords" :class="{'tagSelected':word.selected}"
						 :disable-transitions="false" @click.native="handleTagClick(word)">{{word.word}}</el-tag>
					</div>
				</el-card>
			</el-col>
			<el-col :span="12" v-if="showAnalysisCard">
				<el-card shadow="hover" class="mgb20" style="height: 400px;">
					<ve-pie :data="sentimentsChartData"></ve-pie>
				</el-card>
			</el-col>
			<el-col :span="12" v-if="showAnalysisCard">
				<el-card shadow="hover" class="mgb20" style="height: 250px;">
					<ve-bar :data="sentimentsChartData" :settings="chartSettings" height="260px"></ve-bar>
				</el-card>
				<el-card shadow="hover" class="mgb20" style="height: 130px;">
					<el-alert class="fs18" :title="sentimentStr" :description="sentimentDes" type="error" :closable="false">
					</el-alert>
					<el-alert class="fs10" title="每个词的情感权值都在0~10之间,越接近10说明越积极,反之越消极.数据量越大,结果越精确,情感状态仅供参考" type="info" :closable="false">
					</el-alert>
				</el-card>
			</el-col>
			<el-col :span="8" v-if="showAnalysisCard">
				<el-card shadow="hover" class="mgb20" style="height: 470px;">
					<el-table :data="wordsTableDate" border class="table" @row-click="handleRowClick" size="small">
						<el-table-column prop="name" label="出现词">
						</el-table-column>
						<el-table-column prop="value" label="出现次数">
						</el-table-column>
					</el-table>
				</el-card>
			</el-col>
			<el-col :span="16" v-if="showAnalysisCard">
				<el-card shadow="hover" class="mgb20" style="height: 170px;">
					<div slot="header">
						<span>{{wordInfo.wordTittle}}</span>
					</div>
					<div class="word-info">拼音：<span>{{wordInfo.wordPinyin}}</span></div>
					<div class="word-info">翻译：<span>{{wordInfo.wordTranlation}}</span></div>
					<div class="word-info">释义：<span>{{wordInfo.wordExplanation}}</span></div>
				</el-card>
			</el-col>
			<el-col :span="16" v-if="showAnalysisCard">
				<el-card shadow="hover" class="mgb20" style="height: 280px;">
					<div slot="header">
						<span>高频词分析</span>
					</div>
					<el-alert title="键入的文本量过少,无法分析" type="warning">
					</el-alert>
				</el-card>
			</el-col>
		</el-row>

	</div>
</template>

<script>
	module.exports = {
		name: 'ChineseProcess',
		data: function() {
			return {
				textarea: "",
				enText: "",
				listWords: [],
				Entranslation: "",
				sentiments: {},
				sentiment: 5.0,
				basicDataList: [],
				show: 0,
				isMultSelect: true,
				chartSettings: {
					metrics: ['出现频率'],
					dataOrder: {
						label: '出现频率',
						order: 'desc'
					}
				},
				wordInfo: {
					wordTittle: "详情",
					wordPinyin: "请点击左侧表行",
					wordTranlation: "请点击左侧表行",
					wordExplanation: "暂时无法获取"
				}
			}
		},
		computed: {
			showTranlationCard: function() {
				return (this.show == 2) ? true : false;
			},
			showAnalysisCard: function() {
				return (this.show == 1) ? true : false;
			},
			sentimentsChartData: function() {
				var rows = [];
				for (var i = 0; i < this.sentiments.length; i++) {
					var dateArr = {
						'情感权值': i,
						'出现频率': this.sentiments[i]
					}
					rows.push(dateArr);
				}
				var data = {
					columns: ['情感权值', '出现频率'],
					rows: rows
				}
				return data;
			},
			wordsTableDate: function() {
				if (this.basicDataList.length <= 10) {
					return this.basicDataList;
				} else {
					var data = [];
					for (var i = 0; i < 10; i++) {
						data.push(this.basicDataList[i]);
					}
					return data;
				}
			},
			sentimentStr: function() {
				var str = "除去中性词情感加权平均值为 " + this.sentiment
				return str;
			},
			sentimentDes: function() {
				var str = (this.sentiment - 5.0) > 0 ? "积极" : "消极";
				if (Math.abs(this.sentiment - 5.0) < 1) {
					return "态度基本呈中性偏" + str;
				} else if (Math.abs(this.sentiment - 5.0) > 1 && Math.abs(this.sentiment - 5.0) < 3) {
					return "态度" + str;
				} else {
					return "态度非常" + str;
				}
			},
			isShowButton: function() {
				var data = this.listWords;
				for (var i = 0; i < data.length; i++) {
					if (data[i].selected) {
						return true;
					}
				}
				return false;
			}
		},
		watch: {
			isMultSelect: function() {
				if (!this.isMultSelect) {
					this.listWords = this.handleGetListWord(this.listWords);
				}
			}
		},
		methods: {
			handleAnalysis: function() {
				const loading = this.$loading({
					lock: true,
					text: 'Loading',
					spinner: 'el-icon-loading',
					background: 'rgba(0, 0, 0, 0.7)'
				});
				_this = this;
				var obj = {
					str: this.textarea
				};
				this.$axios({
					method: 'post',
					url: getWordsInfoApi,
					data: obj
				}).then(function(res) {
					_this.show = 1;
					loading.close();
					var result = res.data;
					_this.listWords = _this.handleGetListWord(result.listWords);
					_this.sentiments = result.sentiments;
					_this.sentiment = result.sentiment;
					_this.basicDataList = result.basicDataList;
				}).catch(function() {
					console.log("请求失败");
				});
			},
			handleTranslation: function() {
				var appKey = '445387f4a929dfff';
				var key = '0mWUOpq8RBZipKABAT90eMRgDwY1ea89';
				var salt = new Date().getTime();
				var curtime = Math.round(new Date().getTime() / 1000);
				var from = 'auto';
				var to = 'en';
				var str1 = appKey + this.handleGetInput(this.textarea) + salt + curtime + key;
				var sign = sha256(str1);
				var data = {
					q: this.textarea,
					appKey: appKey,
					salt: salt,
					from: from,
					to: to,
					curtime: curtime,
					sign: sign,
					signType: "v3"
				}
				_this = this;
				$.ajax({
					url: 'http://openapi.youdao.com/api',
					type: 'post',
					dataType: 'jsonp',
					data: data,
					success: function(data) {
						if (data.errorCode == "0") {
							_this.show = 2;
							_this.enText = data.translation[0];
						} else {
							_this.$message.error("请检查网络");
						}
					}
				});
			},
			handleRowClick: function(row, column, event) {
				this.wordInfo.wordTittle = row.name;
				for (var i = 0; i < this.listWords.length; i++) {
					if (this.listWords[i].word == row.name) {
						this.wordInfo.wordPinyin = this.listWords[i].pinyin;
						break;
					}
				}
				var _this = this;
				var appKey = '445387f4a929dfff';
				var key = '0mWUOpq8RBZipKABAT90eMRgDwY1ea89';
				var salt = new Date().getTime();
				var curtime = Math.round(new Date().getTime() / 1000);
				var from = 'auto';
				var to = 'en';
				var str1 = appKey + this.handleGetInput(row.name) + salt + curtime + key;
				var sign = sha256(str1);
				var data = {
					q: row.name,
					appKey: appKey,
					salt: salt,
					from: from,
					to: to,
					curtime: curtime,
					sign: sign,
					signType: "v3"
				}
				_this = this;
				$.ajax({
					url: 'http://openapi.youdao.com/api',
					type: 'post',
					dataType: 'jsonp',
					data: data,
					success: function(data) {
						if (data.errorCode == "0") {
							_this.wordInfo.wordTranlation = data.translation[0];
						} else {
							_this.wordInfo.wordTranlation = "请检查当前网络";
						}
					}
				});
			},
			handleTagClick: function(tag) {
				if (this.isMultSelect) {
					this.handleTagSelect(tag.index);
				} else {
					this.$copyText(tag.word).then(function(e) {}, function(e) {
						_this.$message.error("权限不足，无法复制");
					})
					var Entranslation;
					var appKey = '445387f4a929dfff';
					var key = '0mWUOpq8RBZipKABAT90eMRgDwY1ea89';
					var salt = new Date().getTime();
					var curtime = Math.round(new Date().getTime() / 1000);
					var from = 'auto';
					var to = 'en';
					var str1 = appKey + this.handleGetInput(tag.word) + salt + curtime + key;
					var sign = sha256(str1);
					var data = {
						q: tag.word,
						appKey: appKey,
						salt: salt,
						from: from,
						to: to,
						curtime: curtime,
						sign: sign,
						signType: "v3"
					}
					_this = this;
					$.ajax({
						url: 'http://openapi.youdao.com/api',
						type: 'post',
						dataType: 'jsonp',
						data: data,
						success: function(data) {
							if (data.errorCode == "0") {
								_this.$notify({
									title: tag.word + "(已复制)",
									dangerouslyUseHTMLString: true,
									message: '拼音： ' + tag.pinyin + '<br>英译：' + data.translation[0]
								});
							} else {
								_this.$notify({
									title: tag.word + "(已复制)",
									dangerouslyUseHTMLString: true,
									message: '拼音： ' + tag.pinyin + '<br>英译：' + '暂时无法获取'
								});
							}
						}
					});
				}
			},
			handleGetInput: function(input) {
				if (input.length == 0) {
					return null;
				}
				var result;
				var len = input.length;
				if (len <= 20) {
					result = input;
				} else {
					var startStr = input.substring(0, 10);
					var endStr = input.substring(len - 10, len);
					result = startStr + len + endStr;
				}
				return result;
			},
			handleGetListWord: function(list) {
				var data = [];
				for (var i = 0; i < list.length; i++) {
					var arr = list[i];
					arr.selected = false;
					arr.index = i;
					data.push(arr);
				}
				return data;
			},
			handleTagSelect: function(index) {
				for (var i = 0; i < this.listWords.length; i++) {
					if (this.listWords[i].index == index) {
						this.listWords[i].selected = (this.listWords[i].selected) ? false : true;
					}
				}
			},
			handleSelectedCopy: function() {
				var str = this.handleGetCopyText();
				var _this = this;
				this.$copyText(str).then(function(e) {
					_this.$message.success(str + " 已复制");
				}, function(e) {
					_this.$message.error("权限不足，无法复制");
				})
			},
			handleSelectedTranslation: function() {
				var str = this.handleGetCopyText();
				var _this = this;
				var appKey = '445387f4a929dfff';
				var key = '0mWUOpq8RBZipKABAT90eMRgDwY1ea89';
				var salt = new Date().getTime();
				var curtime = Math.round(new Date().getTime() / 1000);
				var from = 'auto';
				var to = 'en';
				var str1 = appKey + this.handleGetInput(str) + salt + curtime + key;
				var sign = sha256(str1);
				var data = {
					q: str,
					appKey: appKey,
					salt: salt,
					from: from,
					to: to,
					curtime: curtime,
					sign: sign,
					signType: "v3"
				}
				_this = this;
				$.ajax({
					url: 'http://openapi.youdao.com/api',
					type: 'post',
					dataType: 'jsonp',
					data: data,
					success: function(data) {
						if (data.errorCode == "0") {
							_this.$notify({
								title: '翻译',
								dangerouslyUseHTMLString: true,
								message: str + '<br>' + data.translation[0],
								duration: 0
							});
						} else {
							_this.$notify({
								title: '翻译',
								message: "获取失败，请检查网络",
								duration: 0
							});
						}
					}
				});
			},
			handleSelectedSerch: function() {
				var str = this.handleGetCopyText();
				window.open("https://www.google.com/search?q=" + str);
			},
			handleGetCopyText: function() {
				var str = "";
				var data = this.listWords;
				for (var i = 0; i < data.length; i++) {
					if (data[i].selected) {
						str += data[i].word;
					}
				}
				return str;
			}
		},

	}
</script>


<style>
	.el-card__header {
		padding-top: 10px !important;
		padding-bottom: 10px !important;
	}

	.el-tag+.el-tag {
		margin-left: 8px;
		margin-top: 5px;
	}

	.button-new-tag {
		margin-left: 8px;
		height: 32px;
		line-height: 30px;
		padding-top: 0px;
		padding-bottom: 0;
	}

	.input-new-tag {
		width: 90px;
		margin-left: 10px;
		vertical-align: bottom;
	}

	.fs18 .el-alert__title {
		font-size: 18px;
	}

	.fs10 .el-alert__title {
		font-size: 10px;
	}

	.el-alert {
		padding-top: 5px !important;
		padding-bottom: 5px !important;
	}

	.table {
		width: 100%;
		font-size: 14px;
	}

	.word-info {
		font-size: 16px;
		line-height: 30px;
	}

	.word-info span {
		margin-left: 80px;
	}

	.tagSelected {
		background-color: rgba(245, 108, 108, .1) !important;
		border-color: rgba(245, 108, 108, .2) !important;
		color: #f56c6c !important;
	}
</style>
