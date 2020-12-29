![Alt text](ActionSchema.png?raw=true "Schema")

----------
TODO
- WholeVersion не соответствует идеалам разработки, ну и ладно

- TODO(#4)

- Общие TODO
- А правильно ли что запросы шлют не сервисы, а сами компоненты
- localhost в запросах
- проверка на корректность сборки бэка в скрипте update
-    <!-- <expand-block
      *ngIf="isExpandButtonShowed()"
      [isContentShowed]="isContentShowed()"
      (localShowChange)="changeLocalContentShow($event)"
      (globalShowChange)="changeGlobalContentShow($event)"></expand-block> - в group-movement-node и previous-version-node
- Можно выбрать рут при передвижении лифа, надо исправить наследованием/композицией d group-movement-node
- PreviousVersionModalData может не существовать (можно получать версию из сервиса)
- Можно добавить модель для возврата из модалки значения + родитель 
- перенос expandMap в сервис?
- общие стили для иерархии групп

- мув группы и order?

TODO далеко
- Другие типы лифов
- Очень много DTO
- наименования версий
- Лифы в руте
- BehaviorSubject в сервисах?
- Удаление группы ("на совсем")
- а нужно ли валидировать модели на существование? (а где граница между 400 и 500?)