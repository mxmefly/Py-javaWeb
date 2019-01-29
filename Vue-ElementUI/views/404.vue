<template>
    <div class="wscn-http404-container">
        <div class="wscn-http404">
            <div class="pic-404">
                <img class="pic-404__parent" src="assets/images/404/404.png" alt="404">
                <img class="pic-404__child left" src="assets/images/404/404_cloud.png" alt="404">
                <img class="pic-404__child mid" src="assets/images/404/404_cloud.png" alt="404">
                <img class="pic-404__child right" src="assets/images/404/404_cloud.png" alt="404">
            </div>
            <div class="bullshit">
                <div class="bullshit__oops">OOPS!</div>
                <div class="bullshit__info">版权所有
                    <a class="link-type" href="http://www.sunline.cn/" target="_blank">长亮科技股份有限公司</a>
                </div>
                <div class="bullshit__headline">{{ message }}</div>
                <div class="bullshit__info">请检查您输入的网址是否正确，请点击以下按钮返回主页或者发送错误报告</div>
                <a href="" class="bullshit__return-home">返回首页</a>
            </div>
        </div>
    </div>
</template>

<script>

    module.exports = {
        name: 'Page404',
        computed: {
            message: function () {
                return '网管说这个页面你不能进......'
            }
        }
    }
</script>

<style scoped>
    *, *:before, *:after {
        -webkit-box-sizing: inherit;
        box-sizing: border-box;
    }

    .wscn-http404-container {
        transform: translate(-50%, -50%);
        position: absolute;
        top: 40%;
        left: 50%;
    }

    .wscn-http404 {
        position: relative;
        width: 1200px;
        padding: 0 50px;
        overflow: hidden;
    }

    @keyframes cloudLeft {
        0% {
            top: 17px;
            left: 220px;
            opacity: 0;
        }
        20% {
            top: 33px;
            left: 188px;
            opacity: 1;
        }
        80% {
            top: 81px;
            left: 92px;
            opacity: 1;
        }
        100% {
            top: 97px;
            left: 60px;
            opacity: 0;
        }
    }

    @keyframes cloudMid {
        0% {
            top: 10px;
            left: 420px;
            opacity: 0;
        }
        20% {
            top: 40px;
            left: 360px;
            opacity: 1;
        }
        70% {
            top: 130px;
            left: 180px;
            opacity: 1;
        }
        100% {
            top: 160px;
            left: 120px;
            opacity: 0;
        }
    }

    @keyframes cloudRight {
        0% {
            top: 100px;
            left: 500px;
            opacity: 0;
        }
        20% {
            top: 120px;
            left: 460px;
            opacity: 1;
        }
        80% {
            top: 180px;
            left: 340px;
            opacity: 1;
        }
        100% {
            top: 200px;
            left: 300px;
            opacity: 0;
        }
    }

    .pic-404 {
        position: relative;
        float: left;
        width: 600px;
        overflow: hidden;
    }

    .pic-404__parent {
        width: 100%;
    }

    .pic-404__child {
        position: absolute;
    }

    .pic-404__child.left {
        width: 80px;
        top: 17px;
        left: 220px;
        opacity: 0;
        animation-name: cloudLeft;
        animation-duration: 2s;
        animation-timing-function: linear;
        animation-fill-mode: forwards;
        animation-delay: 1s;
    }

    .pic-404__child.mid {
        width: 46px;
        top: 10px;
        left: 420px;
        opacity: 0;
        animation-name: cloudMid;
        animation-duration: 2s;
        animation-timing-function: linear;
        animation-fill-mode: forwards;
        animation-delay: 1.2s;
    }

    .pic-404__child.right {
        width: 62px;
        top: 100px;
        left: 500px;
        opacity: 0;
        animation-name: cloudRight;
        animation-duration: 2s;
        animation-timing-function: linear;
        animation-fill-mode: forwards;
        animation-delay: 1s;
    }

    .bullshit {
        position: relative;
        float: left;
        width: 300px;
        padding: 30px 0;
        overflow: hidden;
    }

    @keyframes slideUp {
        0% {
            transform: translateY(60px);
            opacity: 0;
        }
        100% {
            transform: translateY(0);
            opacity: 1;
        }
    }

    .bullshit__oops {
        font-size: 32px;
        font-weight: bold;
        line-height: 40px;
        color: #1482f0;
        opacity: 0;
        margin-bottom: 20px;
        animation-name: slideUp;
        animation-duration: 0.5s;
        animation-fill-mode: forwards;
    }

    .bullshit__headline {
        font-size: 20px;
        line-height: 24px;
        color: #222;
        font-weight: bold;
        opacity: 0;
        margin-bottom: 10px;
        animation-name: slideUp;
        animation-duration: 0.5s;
        animation-delay: 0.1s;
        animation-fill-mode: forwards;
    }

    .bullshit__info {
        font-size: 13px;
        line-height: 21px;
        color: grey;
        opacity: 0;
        margin-bottom: 30px;
        animation-name: slideUp;
        animation-duration: 0.5s;
        animation-delay: 0.2s;
        animation-fill-mode: forwards;
    }

    .bullshit__return-home {
        display: block;
        float: left;
        width: 110px;
        height: 36px;
        background: #1482f0;
        border-radius: 100px;
        text-align: center;
        color: #ffffff;
        opacity: 0;
        font-size: 14px;
        line-height: 36px;
        cursor: pointer;
        animation-name: slideUp;
        animation-duration: 0.5s;
        animation-delay: 0.3s;
        animation-fill-mode: forwards;
    }

    .link-type {
        cursor: pointer;
        color: inherit;
        text-decoration: none;
    }
</style>
