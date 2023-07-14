# CDKey
paper端的兑换码插件
这个插件依赖floodgate和Xconomy，具有兑换码限时，一次性兑换码，实物和经济奖励的功能。
下面是兑换码文档

这东西还安排一篇文章，针没必要（bushi）
只有腐竹才能新建兑换码
`/newcdk`-新建兑换码字符串
接下来是对兑换码的设置
`/cdk additem`-增加手中物品为兑换码奖励
`/cdk settime`-设置兑换码有效时间，单位天
`/cdk setname`-设置兑换码名称，将会显示在兑换成功提示上
`/cdk setdisposable`-设置兑换码是否是一次性（仅能让一个玩家使用，权重高于有效时间，用完就销毁）
`/cdk setmoney`-设置兑换码钱数，为0则没有
`/cdk finish`-结束本次创建
`/cdk cancel`-取消本次创建

newcdk后所有命令绑定new出来的那个cdk，只有finish或cancel可退出

其次，已存在的cdk不可更改，删除请使用`/cdk delete <兑换码字符串>`即可。

# 玩家兑换
## java玩家
在聊天栏输入`/cdk use <兑换码字符串>`
## 基岩版玩家
在主菜单找到兑换码一项填入并兑换
