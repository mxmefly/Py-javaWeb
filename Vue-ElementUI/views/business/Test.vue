<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-ex-check"></i>用于测试</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-button type="primary" icon="el-icon-edit" circle @click="handleTest"></el-button>
            <el-button type="success" icon="el-icon-check" circle @click="handleTest1"></el-button>
            <el-button type="success" icon="el-icon-check" circle @click="handleTest2"></el-button>
        </div>
    </div>
</template>

<script>
    module.exports = {
        name: 'Test',
        data: function () {
            return {}
        },
        methods: {
            /* 测试 axios 请求 */
            handleTest: function () {
                var obj = {
					str:'就这样的关系，我们持续了三年。六年级的时候，那个男生突然问我了一句——我们这儿是不是马上要拆迁了。这句话让我愣了，我读懂了他话语中的意思，我支支吾吾的不想承认这个事实，瞬间把话题扯到别的事情上了，可我的内心有些预感……那天回家的时候，我也很认真的想过了这个问题，我虽然表面上整天还是像往常一样嘻嘻哈哈作文，但心里已经计算我们还能在一起玩的天数了。这一天我们都没有想到会来的这么快。这天早上，我看到有搬家公司在我们楼底下，心里的预感越发强烈，下楼之后，我看见有好多人都在帮他们一起搬家。我心里默默感叹这一天还是到来了。那个女孩兴致勃勃地跑过来，说：“姐姐，你要不要一起来搬家？”“不了，我还有点事，我作业还没写，我先上去了。”说完，转身就跑走了，我跑进了我的房间，将门反锁起来。可能你们会觉得我连这么点小忙怎么都不会帮，可是我内心却想着是不是我不帮他们，他们就能晚一点走，晚一点离开。差不多十一点半的时候，我听到了那个女孩对那个男生说：“哥哥，再见。”我赶忙跑到窗口上去看，看到的是他们渐行渐远的身影，我终究连一个三年级的小孩子还不如，她都能如此坦然的说了再见，我却做不到，我害怕我们的友谊会因为一句再见而变成了再也不见。我珍惜我们之间的友情，可能方式不同，但因为我心理年龄比他们打，自然想的也就比较多，但我多么想回到曾经纯真的自己，多么想回到曾经最美好的回忆'
			    };
				// obj = JSON.stringify(obj);
                this.$axios({
                    method: 'post',
                    url: getWordsInfoApi,
                    data: obj
                }).then(function (res) {
                    console.log("res", res);
                }).catch(function () {
                    Console.log("请求失败");
                });
            },
            handleTest1: function () {
                console.log("router", this.$router)
            },
            handleTest2: function () {
                console.log("store", this.$store)
            }
        },
        computed: {}
    }
</script>


<style>


</style>

