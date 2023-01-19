import {Component, Directive, ElementRef} from '@angular/core';


@Component({
  selector: 'vk-page',
  template: '<div class="vk-page"><ng-content></ng-content></div>'
})
export class VkPageComponent {}


@Component({
  selector: 'vk-section',
  template: '<div class="vk-section"><ng-content></ng-content></div>'
})
export class VkSectionComponent {}


@Component({
  selector: 'vk-section-header',
  template: '<div class="vk-section-header"><ng-content></ng-content></div>'
})
export class VkSectionHeaderComponent {}


@Component({
  selector: 'vk-buttons',
  template: '<div class="vk-buttons"><ng-content></ng-content></div>'
})
export class VkButtonsComponent {}


@Directive({
  selector: '[vk-button]'
})
export class VkButtonDirective {
  constructor(private el: ElementRef<HTMLButtonElement>) {
    this.el.nativeElement.classList.add("vk-button");
    //TODO ...
  }
}
