import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {

  public testForm: FormGroup = new FormGroup({
    input1: new FormControl(''),
    input2: new FormControl(''),
  });
  public countries: StoreItem[];

  public inputValue1: string;
  public inputValue2: number;

  public info(): string {

    const inputPresent: boolean = this.inputValue1?.length > 0;

    const itemName: string = inputPresent
      ? `Item ${this.inputValue1}`
      : "nothing selected";

    let itemDesc: string = " ";
    if (inputPresent) {
      itemDesc += this.inputValue2?.toString().length > 0
        ? `with price ${this.inputValue2}`
        : "with unknown price"
    }

    return itemName + itemDesc;
  }

  public submit(): void {
    this.inputValue1 = this.testForm.value.input1;
    this.inputValue2 = this.testForm.value.input2;
  }

  ngOnInit(): void {
    this.countries = [
      {name: "fork", prices: [3, 323]},
      {name: "spoon", prices: [432]},
      {name: "kettle", prices: [34324, 434, 423]},
      {name: "stove", prices: [765, 34, 44]},
      {name: "table", prices: [54 , 23]},
      {name: "stool", prices: [324, 111, 1]},
      {name: "chair", prices: [64645, 12312, 55, 34553]},
      {name: "plate", prices: [798, 3]},
      {name: "cup", prices: [6345, 856]},
      {name: "cupcake", prices: [1]},
      {name: "knife", prices: [1, 2]},
      {name: "item1", prices: [6, 78]},
      {name: "item2", prices: [7]},
      {name: "item3", prices: [8]},
      {name: "item4", prices: [9, 10]},
      {name: "item5", prices: [32, 1]},
    ];
  }
}

export interface StoreItem {
  name: string;
  prices: number[];
}
