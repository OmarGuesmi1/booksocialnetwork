import {Component, Input} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-rating',
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './rating.component.html',
  standalone: true,
  styleUrl: './rating.component.scss'
})
export class RatingComponent {

  @Input() raiting : number = 0;
  maxRating: number = 5;

  get fullStars() : number{
    return Math.floor(this.raiting)
  }

  get hasHalfStar(): boolean{
    return this.raiting % 1 !==0;
  }

  get emptyStarts() : number {
    return this.maxRating - Math.ceil(this.raiting);
  }
}
