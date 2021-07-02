# SimpleToDo

## 개요

할 일을 기록하고 D-Day를 정해 날짜가 되면 자동으로 지워주는 앱입니다.

뷰 모델 1개가 모든 작업을 담당하고, 뷰와 뷰 모델의 기능이 제대로 분리되지 않은 부분이 있습니다. 이후 수정하려 합니다.

## 사용된 라이브러리

* AAC
  * Room
  * Data Binding
* RecyclerView
* Glide
* [Rainbow](https://github.com/skydoves/Rainbow)

## 코드

[MainActivity.kt](https://github.com/lht1324/Simple-ToDo/tree/master/app/src/main/java/com/overeasy/simpletodo/view/MainActivity.kt)

메인 액티비티를 다루는 코드입니다. 뷰 모델의 데이터를 받아 어댑터에 넣어주고, 리사이클러 뷰 아이템의 변경을 감지해 뷰 모델에 데이터를 통지해 업데이트합니다.

[ViewModel.kt](https://github.com/lht1324/Simple-ToDo/tree/master/app/src/main/java/com/overeasy/simpletodo/ViewModel.kt)

DB의 내용 업데이트와 DB에서 내용 가져오기, 그리고 데이터를 이용한 계산을 담당하는 뷰 모델에 대한 코드입니다..

[BackgroundMaker.kt](https://github.com/lht1324/Simple-ToDo/tree/master/app/src/main/java/com/overeasy/simpletodo/view/BackgroundMaker.kt)

현재 시간에 따라 배경을 바꿔주는 기능을 BackgroundMaker에 대한 코드입니다. 그라데이션 적용을 위해 Rainbow 라이브러리를 사용했습니다.

[ToDoDialog.kt](https://github.com/lht1324/Simple-ToDo/tree/master/app/src/main/java/com/overeasy/simpletodo/view/ToDoDialog.kt)

할 일의 내용, D-Day를 수정하는 Dialog를 다루는 코드입니다.

[IntroActivity.kt](https://github.com/lht1324/Simple-ToDo/tree/master/app/src/main/java/com/overeasy/simpletodo/view/IntroActivity.kt)

앱을 처음 시작할 때 보여지는 액티비티라 IntroActivity라는 이름을 붙였습니다. 뷰 페이저로 도움말을 보여주며, 이후에도 도움말 버튼을 눌러 다시 열 수 있습니다.

[IntroFragment.kt](https://github.com/lht1324/Simple-ToDo/tree/master/app/src/main/java/com/overeasy/simpletodo/view/IntroFragment.kt)

IntroActivity에서 사용되는 프래그먼트의 코드입니다.

## License

MIT License

Copyright (c) 2021 OverEasy (Jaeho Lee)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
