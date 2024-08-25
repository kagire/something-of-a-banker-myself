import {Component, ElementRef, EventEmitter, forwardRef, HostListener, Input, Output} from '@angular/core';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from "@angular/forms";

@Component({
  selector: 'autocomplete-input',
  templateUrl: './autocomplete-input.component.html',
  styleUrls: ['./autocomplete-input.component.css'],
  providers: [{
    provide: NG_VALUE_ACCESSOR,
    multi: true,
    useExisting: forwardRef(() => AutocompleteInputComponent)
  }]
})
export class AutocompleteInputComponent implements ControlValueAccessor {

  private matchFilter: any = (value: any, o: any) => this.valueFromOption(o)?.toString()?.includes(value.toString());
  private beginFilter: any = (value: any, o: any) => this.valueFromOption(o)?.toString()?.startsWith(value.toString());
  private endFilter: any = (value: any, o: any) => this.valueFromOption(o)?.toString()?.endsWith(value.toString());

  @Input() public placeholder: string;
  @Input() public options: any[] = [];
  @Input() public displayParameter: string;
  @Input() public set filterOption(fo: "begin" | "match" | "end") {
    switch (fo) {
      case "begin": this.filter = this.beginFilter; break;
      case "match": this.filter = this.matchFilter; break;
      case "end": this.filter = this.endFilter; break;
    }
  }

  @Output() public valueChange: EventEmitter<any> = new EventEmitter<any>();
  @Output() public optionAppliance: EventEmitter<any> = new EventEmitter<any>();

  public value: any = "";
  public dropdownVisible: boolean = false;
  public filter: (value: any, o: any) => boolean = this.matchFilter;

  onChange = (value) => {};
  onTouched = () => {};
  touched = false;
  disabled = false;

  constructor(private elementRef: ElementRef) {}

  @HostListener('document:click', ['$event'])
  public clickOutside(event: any): void {
    if(!this.elementRef.nativeElement.contains(event.target)) {
      this.dropdownVisible = false;
    }
  }

  public availableOptions(): any[] {
    return this.value ? this.options.filter(o => this.filter(this.value, o)) : [];
  }

  public setValue(value: any): void {
    this.value = value;
    this.valueChange.emit(this.value);
    this.onChange(value);
  }

  public applyOption(option: any): void {
    this.setValue(this.valueFromOption(option));
    this.optionAppliance.emit(option);
  }

  public valueFromOption(option: any): any {
    return this.displayParameter ? option[this.displayParameter] : option;
  }

  registerOnChange(onChange: any) {
    this.onChange = onChange;
  }

  registerOnTouched(onTouched: any) {
    this.onTouched = onTouched;
  }

  writeValue(obj: any) {
    this.value = obj;
  }

  markAsTouched() {
    if (!this.touched) {
      this.onTouched();
      this.touched = true;
    }
  }

  setDisabledState(disabled: boolean) {
    this.disabled = disabled;
  }
}
