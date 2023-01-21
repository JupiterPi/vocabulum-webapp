import {Component, Directive, HostBinding, Input} from '@angular/core';


@Component({
  selector: 'vk-page',
  template: `
    <div class="vk-page"
         [ngClass]="size == 'slim' ? 'vk-page-slim' : 'vk-page-regular'"
    >
      <ng-content></ng-content>
    </div>
  `
})
export class VkPageComponent {
  @Input() size: "regular" | "slim" = "regular";
}


@Component({
  selector: 'vk-section',
  template: '<div class="vk-section"><ng-content></ng-content></div>'
})
export class VkSectionComponent {}


@Component({
  selector: 'vk-section-header',
  template: `
    <div class="vk-section-header"
         [class.vk-section-header-small]="textSize == 'small'"
         [class.vk-section-header-medium]="textSize == 'medium'"
         [class.vk-section-header-large]="textSize == 'large'"
    ><ng-content></ng-content></div>
  `
})
export class VkSectionHeaderComponent {
  @Input("size") textSize: "small" | "medium" | "large" = "medium";
}


/* ----- buttons ----- */


@Component({
  selector: 'vk-buttons',
  template: '<div class="vk-buttons"><ng-content></ng-content></div>'
})
export class VkButtonsComponent {}


@Component({
  selector: 'vk-button',
  template: `
    <div class='vk-primary-button-container'>
      <button
        mat-ripple [disabled]="disabled" [matRippleDisabled]="disabled"
        [ngClass]="variant == 'basic' ? 'vk-basic-button' : 'vk-primary-button'"
        [class.vk-basic-button-black]="color == 'black'"
        [class.vk-basic-button-red]="color == 'red'"
      >
        <ng-content></ng-content>
      </button>
    </div>
  `
})
export class VkButtonComponent {
  @Input() variant: "basic" | "primary" = "basic";
  @Input() color: "none" | "black" | "red" = "none";
  @Input() disabled = false;
}


/* ----- meta ----- */


@Component({
  selector: 'vk-meta-container',
  template: '<div class="vk-meta-container"><ng-content></ng-content></div>'
})
export class VkMetaContainerComponent {}


@Component({
  selector: 'vk-meta',
  template: '<div class="vk-meta"><ng-content></ng-content></div>'
})
export class VkMetaComponent {}


@Component({
  selector: 'vk-meta-key',
  template: '<div class="vk-meta-key"><ng-content></ng-content></div>'
})
export class VkMetaKeyComponent {}


@Component({
  selector: 'vk-meta-value',
  template: '<div class="vk-meta-value"><ng-content></ng-content></div>'
})
export class VkMetaValueComponent {}


/* ----- input ----- */


@Directive({
  selector: '[vk-input]'
})
export class VkInput {
  @HostBinding("class") className = "vk-input";
}
