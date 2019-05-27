相关代码，见我的开源项目:
[https://github.com/qianxingchuan/doraemon](https://github.com/qianxingchuan/doraemon)


##　简介

2017年，我在菜鸟物流云这个部门做了一个叫做Asgard的网关，当时为了快速实现，只适配了阿里巴巴的技术栈体系，比如鉴权的部分，直接把菜鸟的账号系统接入的代码固化在网关；再比如调用服务的部分，直接把泛化调用HSF的代码固化在网关。这些固化代码的方式，对于Asgard的可扩展性和灵活性都造成了无比巨大的限制。

从菜鸟离职之后，在2019年５月，在vivo做关于云存储相关的项目，vivo的基础设施不是特别完美，所以存在很多重复劳动，比如几乎每个web应用都会写一套一模一样的app鉴权、web鉴权，以及通用的cors的配置。这一点我非常不爽，所以决定在vivo重新把Asgard写一遍，这次重写，必定完善之前菜鸟物流云时期没有时间做的一些事情，比如高度可自定义每个调用阶段，再比如网关必须要支持响应式、全异步，以及支持websocket等等。

既然要实现高度自定义，那么我们需要有一套高度可插拔的plugin处理框架，osgi太重了，我不想我们的应用有太多这样的依赖，以及发布规范; 蚂蚁金服有一套开源的sofa-ark，也能满足我的要求，但是对于我来说，还是不够轻量级，我其实只要能在网关应用内部可以独立每个plugin即可，如图１：
![图１](https://raw.githubusercontent.com/qianxingchuan/doraemon/master/document/arch.jpg)

本文重点介绍我基于Java类加载器实现的一个轻量级模块隔离框架:Doraemon。
在同一个JVM里面，我们的应用程序可以调用任意一个bundle的export出来的实例，bundle互相之间不可见。

##　Doraemon快速使用
### 实现基于鉴权接口的不同实现

样例工程见doraemon-sample

主要工程:
sample-auth-facade : 鉴权的接口定义
sample-auth-bundle1 : 基于鉴权接口的实现１
sample-auth-bundle2 : 基于鉴权接口的实现２
sample-auth-project : 运行bundle1和bundle2的主程序

## bundle的实现

1. 可以基于doraemon-project-archetype来构建你的代码骨架
2. 生成的doraemon-bundle目录结构如图２
![图２](https://raw.githubusercontent.com/qianxingchuan/doraemon/master/document/bundle-description.png)

bundle 的 pom.xml关键依赖如下：
```
...

    <dependencies>
        <!--业务的基本依赖 -->
        <dependency>
            <groupId>io.github.qianxingchuan.framework</groupId>
            <artifactId>sample-auth-facade</artifactId>
        </dependency>
        <!--每个bundle的依赖，每个bundle 必须要有一个 io.github.qianxingchuan.framework.doraemon.BundleService实例 -->
        <dependency>
            <groupId>io.github.qianxingchuan.framework</groupId>
            <artifactId>doraemon-facade</artifactId>
            <version>0.1-RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.1.4.RELEASE</version>
        </dependency>
    </dependencies>
...
</project>
```

bundle内部的实现，不限制框架，因为doraemon在运行每个bundle的时候是互相隔离的，但是每个bundle必须要有一个类来实现io.github.qianxingchuan.framework.doraemon.BundleService,并且配置到 bundle.properties

bundle.properties配置如下：
```
init-class=io.github.qianxingchuan.doraemon.sample.auth.run.SampleBundleRun
skip-class=io.github.qianxingchuan.doraemon.sample.facade.AuthFacade
```
init-class　的意思就是bundle在初始化会自动执行这个里面的doIt方法
skip-class 的意思是该class不会由bundle的类加载器来加载

所以按照我们图１的表述，这个配置就是SampleBundleRun由bundle1的模块类加载器来加载，AuthFacade则由Application所在的类加载器来加载。

所有的代码写完之后，通过mvn clean compile package，即可生成一个 .zip 的bundle文件。

## bundle运行

参照sample-auth-project工程


