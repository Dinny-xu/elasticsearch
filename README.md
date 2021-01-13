# ElasticSearch 搭建
- Download Elasticsearch
https://www.elastic.co/cn/downloads/elasticsearch

- 注意：
    - ES服务版本必须与Maven 版本一致
    - 启动ES 访问默认端口
    
```
http://localhost:5601
```
## 创建索引库

```
PUT study
{
   "mappings": {
      "properties": {
         "content": {
            "type": "text",
            "analyzer": "ik_max_word",
            "search_analyzer": "ik_smart"
         },
         "title": {
            "type": "text",
            "analyzer": "ik_max_word",
            "search_analyzer": "ik_smart"
         },
         "createDate": {
            "type": "long"
         }
      }
   }
}
```

- study : 索引库名称
- mappings : 查询字段
- content : 内容
- type : 字段类型  text(文本类型)
- analyzer参数指定索引或搜索字段时用于 文本分析的分析器。text

### 详细请参考官网文档
https://www.elastic.co/guide/index.html