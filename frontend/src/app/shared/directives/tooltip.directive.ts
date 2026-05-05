import { Directive, ElementRef, inject, input, OnDestroy } from '@angular/core';
import { Overlay, OverlayRef } from '@angular/cdk/overlay';
import { ComponentPortal } from '@angular/cdk/portal';
import { TooltipContentComponent } from '../components/tooltip-content/tooltip-content.component';

@Directive({
  selector: '[appTooltip]',
  host: {
    '(mouseenter)': 'show()',
    '(mouseleave)': 'hide()',
    '[attr.aria-label]': 'text()',
    '[class.cursor-help]': 'true'
  }
})
export class TooltipDirective implements OnDestroy {
  text = input.required<string>({ alias: 'appTooltip' });
  type = input<string>('bg-blue-600', { alias: 'tooltipType' });

  private overlay = inject(Overlay);
  private elementRef = inject(ElementRef);
  private overlayRef?: OverlayRef;

  show() {
    if (this.overlayRef) return;

    const positionStrategy = this.overlay.position()
      .flexibleConnectedTo(this.elementRef)
      .withPositions([
        { originX: 'center', originY: 'top', overlayX: 'center', overlayY: 'bottom', offsetY: -8 },
        { originX: 'center', originY: 'bottom', overlayX: 'center', overlayY: 'top', offsetY: 8 }
      ]);

    this.overlayRef = this.overlay.create({
      positionStrategy,
      scrollStrategy: this.overlay.scrollStrategies.reposition(),
    });

    const portal = new ComponentPortal(TooltipContentComponent);
    const componentRef = this.overlayRef.attach(portal);

    // Set the input on the dynamic component
    componentRef.instance.text = this.text;
    componentRef.instance.type = this.type ?? this.type;
  }

  hide() {
    this.cleanup();
  }

  ngOnDestroy() {
    this.cleanup();
  }

  private cleanup() {
    if (this.overlayRef) {
      this.overlayRef.dispose();
      this.overlayRef = undefined;
    }
  }
}
