import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

import { AppComponent } from './app.component';
import { VersionsListModule } from './page/versions-list/versions-list.module';
import { AppRoutingModule } from './app-routing.module';
import { WholeVersionModule } from './page/whole-version/whole-version.module';
import { Preloader } from './preloader/preloader.component';
import { ErrorModule } from 'app/error/error.module';
import { DifferenceModule } from 'app/page/difference/difference.module';
import { LoginModule } from './page/login/login.module';

@NgModule({
  declarations: [
    AppComponent,
    Preloader,
  ],
  imports: [
    VersionsListModule,
    WholeVersionModule,
    DifferenceModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NoopAnimationsModule,
    ErrorModule,
    LoginModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
