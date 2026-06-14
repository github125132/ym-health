var pageCache = {};

pageCache['plugin.php' + window.location.search] = $('#container').html();

$('#head-slide').on('click', '.slide-items', function () {
	$(this).siblings().removeClass('selected');
	$(this).addClass('selected');
	var rect = this.getBoundingClientRect();
	var width = $(document).width();
	var scrollLeft = $('#head-slide').scrollLeft();
	var title = this.innerText;
	var to_url = $(this).data('url');
	if (rect.right + 40 > width) {
		$('#head-slide').animate({scrollLeft: scrollLeft + rect.right - width + 130}, 200);
	}
	if (rect.left < 40) {
		$('#head-slide').animate({scrollLeft: scrollLeft + rect.left - 130}, 200);
	}
	popup.open('<img src="' + IMGDIR + '/imageloading.gif" style="margin: 0 auto;display: block;">');
	$(window).unbind("scroll");
	if(pageCache[to_url] !== undefined){
		$('#container').html(pageCache[to_url]);
		ReBindSwipe();
		popup.close();
		window.history.pushState({url : to_url, container: '#container', data: pageCache[to_url], title: title}, title, to_url);
	} else {
		$.ajax({
			url: to_url + '&inajax=2',
			data: {},
			type: 'get',
			dataType: 'xml',
			success:function(data) {
				data = data.lastChild.firstChild.nodeValue
				pageCache[to_url] = data;
				$('#container').html(data);
				ReBindSwipe();
				document.title = title;
				popup.close();
				window.history.pushState({url : to_url, container: '#container', data: data, title: title}, title, to_url);
			},
		});
	}

});
$(window).bind('popstate', function(event){
	var state = event.originalEvent.state
	if (state && state.container) {
		if(typeof state.url !== 'undefined') {
			var url = /.*\/([^/]*)$/.exec(state.url);
			var url = url != null ? url[1] : state.url;
			var res = $('.head-slides').find('.slide-items[data-url="'+url+'"]');
			if(res.length > 0) {
				$('.head-slides').children().removeClass('selected')
				$(res.get(0)).addClass('selected');
			}
		}
		$('#container').html(state.data);
		ReBindSwipe();
	}
});
$('#container').attr('data-am-observe', 'true');
function ReBindSwipe(){
	$('#container>:not(.no_swipe)').on('movestart', function(e) {
		if ((e.distX > e.distY && e.distX < -e.distY) || (e.distX < e.distY && e.distX > -e.distY)) {
			e.preventDefault();
		}
	})
	.on('move', function(e) {
		var left = 100 * e.distX / $(window).width();
		$('#container').css('left', left + '%');
	})
	.on('moveend', function(e) {
		var left = $('#container').css('left').replace('px', '');
		if(Math.abs(left) < $(window).width() * 0.4 || (left > 0 && $('.head-slides .selected').prev().length == 0) || (left < 0 && $('.head-slides .selected').next().length == 0)) {
			$('#container').animate({'left':0}, 100);
		}
	})
	.on('swiperight', function() {
		if($('.head-slides .selected').prev().length > 0) {
			$('#guodu').removeClass('hide');
			$('#container').animate({left: '100%'}, 200, 'easeOutQuint', function (){
				$('#container').html('');
				$('#container').css('left', 0);
				$('.head-slides .selected').prev().get(0).click();
			});
		}
	})
	.on('swipeleft', function() {
		if($('.head-slides .selected').next().length > 0) {
			$('#guodu').removeClass('hide');
			$('#container').animate({left: '-100%'}, 200, 'easeOutQuint', function (){
				$('#container').html('');
				$('#container').css('left', 0);
				$('.head-slides .selected').next().get(0).click();
			}); 
		}
	});
	$("img.lazy").lazyload({
		effect : "fadeIn"
	});
	if(typeof AMUI != "undefined") {
		AMUI.menu.init();
		AMUI.slider.init();
		AMUI.tab.init();

	}
}
ReBindSwipe();