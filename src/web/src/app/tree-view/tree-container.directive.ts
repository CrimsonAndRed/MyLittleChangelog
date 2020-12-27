import { Directive, TemplateRef, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[root-container]'
})
export class RootContainerDr {
  constructor(public tpl: TemplateRef<any>, public viewContainerRef: ViewContainerRef) { }
}

@Directive({
  selector: '[group-container]'
})
export class GroupContainerDr {
  constructor(public tpl: TemplateRef<any>, public viewContainerRef: ViewContainerRef) { }
}