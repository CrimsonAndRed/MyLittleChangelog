import { Directive, ViewContainerRef } from '@angular/core';

@Directive({
  selector: '[global-header]'
})
export class GlobalHeader {
}

@Directive({
  selector: '[group-header]'
})
export class GroupHeaderDr {
  constructor(public viewContainerRef: ViewContainerRef) { }
}

@Directive({
  selector: '[leaf-header]'
})
export class LeafHeaderDr {
  constructor(public viewContainerRef: ViewContainerRef) { }
}
