![Alt text](ActionSchema.png?raw=true "Schema")

----------
TODO
- Валидация
- Исправить transaction в тестах 
- Исправить transaction в репозиториях
- несколько commit в тестах
- WholeVersion не соответствует идеалам разработки, ну и ладно
- Если в сервисе мутируется Entity, то ломается мок
- Тест на неправильные параметры URL
- URL в тестах с / или нет?

- Удаление группы ("на совсем")
- TODO(#1)
- TODO(#2)
- Очень много DTO
- TODO(#4)
- кэш запросов past version?
- Лифы в руте
- порядок показа групп в версии?
- TODO(#7) получение текущей версии через сервис?
- TODO(#8) groupVid в LeafContent?
- TODO(#10) притягиваю в service router, это не верно
- При добавлении узла из прошлого в принципе можно не перезапрашивать дерево с бэка, а рисовать его фронтом
- Пересортировка (после реализации сортировки) + сортировка дематериализованных групп будет ломать предыдущие версии?
- delete group не происходит, если у группы есть дематериализованная подгруппа