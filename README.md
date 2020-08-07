# myblog2
之前写过一个前后端分离的Vue + SSM 的个人博客 放了几个月自己实在是看不下去了 不论是从样式 速度 都是让我自己我懒得看的玩意

于是重构了一个版本 从结果上来看这次更新的结果要比上一次好很多 但是做完也有点四不像 ajax的走ajax 模板渲染的走模板渲染

现在看来最好的构建方式还是该使用Vue来做的



# 实现的功能

1. 网站能展示的内容的增删改查（二维码和联系方式写死了 主要是懒得写了)
2. 文章展示 目录生成 留言 
3. 做成了一个小论坛形式 即 每个人都可以注册登录发文 
4. 引入了简单的权限控制 管理员有资格对网站的用户进行权限限制
5. 图片上传 可以上传图片和头像
6. 接入了第三方登录(QQ 和 Github登录)
7. 访问量和访问纪录统计
8. 接入缓存 虽然缓存这里做的有点烂

# 博客构建导的包..😓😓

## 前端

1. bootstrap + jquery 
2. 代码高亮 [prism](https://github.com/PrismJS/prism) highlight js 其实也能够 但是highlight js的
3. markdown编辑器 [editormd  ](https://pandao.github.io/editor.md/) 
4. 目录生成 katelog(https://github.com/KELEN/katelog) 
5. 标签颜色选择 [Bootstrap Colorpicker](https://github.com/itsjavi/bootstrap-colorpicker)
6. 二维码生成 [qrcodejs](https://github.com/davidshimjs/qrcodejs)
7. 让文章好看点 [typo](https://github.com/sofish/typo.css)

## 后端

1. SpringBoot
2. Mysql
3. Redis 缓存
4. Mybatis 持久层
5. shiro  权限控制
6. OkHttp 完成Auth2认证
7. 模板引擎 thymeleaf
8. 分页 PageHelper 
9. 还有使用Hutool 和 Hibernate的表单检验

技术都假的 对着包使劲导才是真的

# 样式展示

样式是对着B站[这个教程](https://www.bilibili.com/video/BV1nE411r7TF) 然后使用Bootstrap来进行制作的

![1](https://github.com/VMJWV/myblog2/blob/master/pictures/1.png)

![2](https://github.com/VMJWV/myblog2/blob/master/pictures/2.png)

![3](https://github.com/VMJWV/myblog2/blob/master/pictures/3.png)

象征性贴几张 网站已经部署 具体样式可以前往查看

[constme](http://www.constme.cn)

# 缺点

1. 前端水平较低 不论是html css js 都写的稀烂
2. 制作中临时学了个Shiro 突发奇想要接入权限控制 所以Service和Mapper可能看起来比较难过
3. 制作中临时学了Redis和SpringBoot缓存注解 但是并没有比较好的缓存方案 所以会缓存所有内容 这也就意味着在更新的时候要用AOP切入进去清楚所有缓存 所以**缓存其实是做的很烂的**
4. 用AOP把所有的Controller都切入了 导致访问纪录会非常混乱
5. 只进行了简单测试 不排除还存在Bug
6. 不知道是啥了 反正技术菜 啥都是缺点

## 总结

加油导包 做一个导包最快的CRUD仔





