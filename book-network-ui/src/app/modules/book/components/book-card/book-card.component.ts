import {Component, EventEmitter, input, Input, Output} from '@angular/core';
import {BookResponse} from '../../../../services/models/book-response';
import {NgIf} from '@angular/common';
import {RatingComponent} from '../rating/rating.component';

@Component({
  selector: 'app-book-card',
  imports: [
    NgIf,
    RatingComponent
  ],
  templateUrl: './book-card.component.html',
  standalone: true,
  styleUrl: './book-card.component.scss'
})
export class BookCardComponent {
  private _manage: boolean = false;
  private _bookCover: string | undefined;


  get manage(): boolean {
    return this._manage;
  }

  @Input()
  set manage(value: boolean) {
    this._manage = value;
  }

  private _book: BookResponse = {};

  get book(): BookResponse {
    return this._book;
  }

  @Input()
  set book(value: BookResponse) {
    this._book = value;
  }


  get bookCover(): string | undefined {
    if (this._book.cover){
      return 'data:image/jpg;base64, ' + this._book.cover
    }

    return 'https://picsum.photos/1900/800'
    this._bookCover;
  }
  @Output() private share: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private archive: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private addToWaitingList: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private borrow: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private edit: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private details: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();

  onShowDetails() {
  this.details.emit(this._book)
  }

  onBorrow() {
    this.borrow.emit(this._book)

  }

  onAddToWaitingList() {
    this.addToWaitingList.emit(this._book)

  }

  onEdit() {
    this.edit.emit(this._book)

  }

  onShare() {
    this.share.emit(this._book)

  }

  onArchive() {
    this.archive.emit(this._book)

  }
}
