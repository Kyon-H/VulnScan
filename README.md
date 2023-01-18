# VulnScan

## 数据库设计

根据上述需求，我们可以设计如下数据库：

数据库名：vulnerability_scanner，字符集：utf8mb4，排序规则：utf8mb4_unicode_ci 

1. 用户表（user）：记录用户信息，包括用户名、密码、邮箱、角色等字段。
2. 扫描记录表（scan_record）：记录扫描记录信息，包括扫描类型（主动或被动）、扫描网站地址、扫描时间、用户名等字段。
3. 漏洞信息表（vulnerability_info）：记录漏洞信息，包括漏洞名称、级别、详细描述、修复建议等字段。
4. 漏洞扫描结果表（vulnerability_result）：记录漏洞扫描结果，包括扫描记录ID、漏洞信息ID、状态（已修复或未修复）等字段。
5. 扫描报告表（scan_report）：记录扫描报告信息，包括扫描记录ID、报告文件名、生成时间等字段。

```mysql
CREATE DATABASE vulnscan
USE vulnscan
-- 用户表
CREATE TABLE user_info (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) ,
  password VARCHAR(255) ,
  email VARCHAR(255) ,
  role VARCHAR(255) ,
  create_time DATETIME 
)COMMENT='用户表';

-- 目标记录表
CREATE TABLE target_info(
	id INT PRIMARY KEY AUTO_INCREMENT,
    address VARCHAR(255) COMMENT 'URL',
    canonical_address VARCHAR(255) COMMENT '规范地址',
    domain VARCHAR(255),
    criticality INT COMMENT '危险程度',
    target_type VARCHAR(255),
    description VARCHAR(255),
    target_id VARCHAR(255),
    type VARCHAR(255),
    canonical_address_hash VARCHAR(255)
);
-- 扫描记录表
CREATE TABLE scan_record (
  id INT PRIMARY KEY AUTO_INCREMENT,
  type VARCHAR(255) ,
  website_url VARCHAR(255) ,
  scan_time DATETIME ,
  username VARCHAR(255) 
)COMMENT='扫描记录表';

-- 漏洞信息表
CREATE TABLE vuln_info (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) ,
  level VARCHAR(255) ,
  description TEXT ,
  fix_suggestion TEXT 
)COMMENT='漏洞信息表';

-- 漏洞扫描结果表
CREATE TABLE vuln_result (
  id INT PRIMARY KEY AUTO_INCREMENT,
  scan_record_id INT ,
  vulnerability_info_id INT ,
  status VARCHAR(255) 
)COMMENT='漏洞扫描结果表';
-- FOREIGN KEY (scan_record_id) REFERENCES scan_record(id),
--  FOREIGN KEY (vulnerability_info_id) REFERENCES vulnerability_info(id)

-- 扫描报告表
CREATE TABLE scan_report (
  id INT PRIMARY KEY AUTO_INCREMENT,
  scan_record_id INT ,
  report_filename VARCHAR(255) ,
  generate_time DATETIME 
)COMMENT='扫描报告表';
-- FOREIGN KEY (scan_record_id) REFERENCES scan_record(id)
```



## renren-fast-vue

VSCode打开文件夹，控制台运行命令：

```cmd
npm install
# 无ERR 运行
npm run dev
```

问题：==npm ERR! Cannot read properties of null (reading 'pickAlgorithm')==

==npm ERR! A complete log of this run can be found in:
npm ERR!     C:\Users\LOAD\AppData\Local\npm-cache\_logs\2023-01-01T12_46_14_465Z-debug-0.log==

解决方法：以管理员身份运行，运行命令`npm cache clear --force`运行命令`npm install --platform=win32` 



## VulnScan

### 表单校验、分组校验：JSR303

```xml
<!-- javax.validation -->
<dependency>
    <groupId>javax.validation</groupId>
    <artifactId>validation-api</artifactId>
</dependency>
```



```
package javax.validation.constraints 包下注解
Entity成员添加注解，可定制报错信息
@Email(message="email格式不正确",groups={addGroup.class,updateGroup.class})
不为空
@NotBlank 至少包含一个空格
@NotNull 验证注解的元素值不是null
@NotEmpty 验证注解的元素值不为null且不为空（字符串长度不为0、集合大小不为0）
@Pattern(regexp="") 正则表达式，自定义校验
```

```java
//Controller形参添加@Valid开启校验。紧跟校验参数后加 BindingResult result 获取校验结果
//使用@Validated({})进行分组校验
public R Login(@Valid @RequestBody UsersEntity user, BindingResult result){
    if(result.hasErrors()){
            //数据校验不通过
        }else {
            //数据校验通过
        }
}
```

### 统一异常处理

**@ControllerAdvice**

```java
//新建ControllerAdviceException.java，指定异常处理范围
@ControllerAdvice(basePackages = "com.atlxc.VulnScan.product.controller")
@ExceptionHandler(value = MethodArgumentNotValidException.class)
public R handleValidException(MethodArgumentNotValidException exception){}
```



## VulnScan-vue

全局安装webpack、cli

```
npm install webpack -g
npm install -g @vue/cli-init@2.9.6
```

初始化项目

```
vue init webpack vulnscan-vue
```

### Node.js安装更新

1. 官网下载：[Download | Node.js (nodejs.org)](https://nodejs.org/en/download/)

2. 安装，更改路径`D:\software\nodejs`，其余默认方式安装

3. **更改缓存路径和模块路径**

   ==默认路径：C:\Users\用户名\AppData\Roaming\npm==

   1. 将全局模块（node_global）和缓存（node_cache）放在nodejs安装目录下。

      在`D:\Program Files\nodejs`目录下新建`node_global`、`node_cache`文件夹，在`node_global`下新建`node_modules`文件夹

   2. cmd窗口输入命令：

      ```
      npm config set prefix "D:\software\nodejs\node_global"
      npm config set cache "D:\software\nodejs\node_cache"
      ```

   3. 在nodejs的安装目录下，进入node_modules—>npm—>找到`.npmrc`文件，写入

      ```
      prefix=D:\software\nodejs\node_global
      cache=D:\software\nodejs\node_cache
      ```

4. 设置环境变量

   新建系统变量，变量名：`NODE_PATH`，变量值：`D:\software\nodejs\node_global\node_modules` *（node_global文件夹下的node_modules文件夹）*。

   修改用户变量，将默认的npm路径修改为新建的node_global路径，变量名：`Path`，变量值：`D:\software\nodejs\node_global`

### 解决跨域

config/index.js 修改dev下的proxyTable

```js
proxyTable: {
      '/api': {
        target:'http://127.0.0.1:8080',
        // secure: false, // 如果是https接口，需要配置这个参数
        changeOrigin:true,
        pathRewrite:{
          '^/api': ''
        }
      }
    },
```

