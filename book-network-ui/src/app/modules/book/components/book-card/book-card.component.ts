import {Component, Input} from '@angular/core';
import {BookResponse} from '../../../../services/models/book-response';

@Component({
  selector: 'app-book-card',
  imports: [],
  templateUrl: './book-card.component.html',
  standalone: true,
  styleUrl: './book-card.component.scss'
})
export class BookCardComponent {

  private _book: BookResponse = {};

  get book(): BookResponse {
    return this._book;
  }

  @Input()
  set book(value: BookResponse) {
    this._book = value;
  }

  private _bookCover: string | undefined;

  get bookCover(): string | undefined {
    if (this._book.cover){
      return 'data:image/jpg;base64, ' + this._book.cover
    }

    return 'https://picsum.photos/1900/800'
    this._bookCover;
  }

}
