# elasticsearch
ES搜索搭建
创建索引

```html
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