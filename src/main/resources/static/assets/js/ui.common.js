"use strict";

(function () {
  var $window = $(window);
  var $document = $(document);
  var $this = $(this); //main menu

  let vh = window.innerHeight * 0.01;
  document.documentElement.style.setProperty("--vh", `${vh}px`);
  window.addEventListener("resize", () => {
    //console.log("resize");
    let vh = window.innerHeight * 0.01;
    document.documentElement.style.setProperty("--vh", `${vh}px`);
  });
  $(function () {
    //profile
    $(".user__anchor").slideUp();
    $document.on("mouseenter", ".user__hover", function () {
      $(".user__anchor").slideDown();
    });
    $document.on("mouseleave", ".user__anchor", function () {
      $(".user__anchor").slideUp();
    });
  });
  $(document.body).on('click', '.js-modal-alert', function (e) {
    e.preventDefault();
    var popName = $($(this).attr("href")),
        popAnchor = $($(this).data("target"));
    console.log(popName);
    $('html').css({// "overflow": "hidden"
    });
    popName.before("<div class='dimed'></div>");
    popName.addClass("is-on");
  });
  $(document.body).on('click', '.pop__detail .js-modal-close', function () {
    $(".dimed").remove();
    $(this).parents().removeClass("is-on");
    $('html').css({//'overflow': 'auto'
    });
  }); //새 미팅룸 만들기 - 참석자 추가시 input focus
  //개발시 변경 가능

  $(document.body).on('focusin', '#make_team', function () {
    $(".flow__team").slideDown();
  });
  $(document.body).on('mouseleave', '.flow__team', function () {
    $(".flow__team").slideUp();
  });
})();