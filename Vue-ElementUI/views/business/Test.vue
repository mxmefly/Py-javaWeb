<template>
    <div >
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-ex-check"></i>用于测试</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container" >
			<el-button type="primary" icon="el-icon-edit" circle @click="handleTest"></el-button>
			<el-button type="success" icon="el-icon-check" circle @click="handleTest1"></el-button>
			<el-button type="success" icon="el-icon-check" circle @click="handleTest2"></el-button>
        </div>
    </div>
</template>

<script>
    module.exports = {
        name: 'Test',
        data:function() {
            return {
                
            }
        },
        methods: {
			/* 测试 axios 请求 */
           handleTest:function(){
			   var obj=[];
			   this.$axios({
				   method: 'get',
				   url:Demo_Apis.Test_Url,
				   data: obj
			   }).then(function(res){
				   console.log("res",res);
			   }).catch(function(){
				   Console.log("请求失败");
			   });
		   },
		   handleTest1:function(){
			   console.log("base",BASE_API)
		   },
		   handleTest2:function(){
			   console.log("store",this.$store)
		   }
        },
        computed: {
           
    }
}
</script>


<style>


</style>

