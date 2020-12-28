![Alt text](ActionSchema.png?raw=true "Schema")

----------
TODO
- WholeVersion не соответствует идеалам разработки, ну и ладно

- TODO(#4)

- реструктуризация фронта

- move-movement можно схлопнуть обратно но используя как input функцию проставляющую контекст
- дифференсные группы и лифы и groups-sec - это одна структура или нет?
- Общие TODO
- В сервисах на фронте модифицируется существующий элемент вместо Observera от нового элемента (например, в versions-list.service)
- сервисы должны формировать специальные subject'ы, на которые можно не только сабскрайбаться, но и получать последнее значение
- А правильно ли что запросы шлют не сервисы, а сами компоненты
- localhost в запросах
- проверка на корректность сборки бэка в скрипте update
- addGroupToParent в wholeVersionService не работает
-    <!-- <expand-block
      *ngIf="isExpandButtonShowed()"
      [isContentShowed]="isContentShowed()"
      (localShowChange)="changeLocalContentShow($event)"
      (globalShowChange)="changeGlobalContentShow($event)"></expand-block> - в group-movement-node
- Можно выбрать рут при передвижении лифа, надо исправить наследованием/композицией d group-movement-node
     
- мув группы и order?

TODO далеко
- Другие типы лифов
- Очень много DTO
- наименования версий
- Лифы в руте
- Удаление группы ("на совсем")
- а нужно ли валидировать модели на существование? (а где граница между 400 и 500?)