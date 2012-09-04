function Slider() {

}

Slider.prototype = {
	btnPrev : "#sliderPre",
	btnNext : "#sliderNext",

	speed : 500,
	easing : null,

	visible : 4,
	start : 0,
	scrollNo : 1,
	hasNext : true,
	hasPre : false,
	itemLength : 0,
	animCss : "left",
	sizeCss : "width",
	liSize : 0,
	running : false,

	onClickImage : function(index) {
		if (index <= this.start+1) {
			return this.pre();
		} else if (index >= this.start + this.visible - 2) {
			return this.next();
		}
	},

	pre : function() {
		console.log("this.start=" + this.start + " -  " + this.scrollNo);
		return this.go(0, this.start - this.scrollNo);
	},
	next : function() {
		console.log("this.start=" + this.start + " +  " + this.scrollNo);
		return this.go(1, this.start + this.scrollNo);
	},

	setRunning : function(isRuning) {
		this.running = isRuning;
	},

	go : function(type, to) {
		console.log("Go : " + to);
		console.log("Before Go, is Running?" + this.running);

		if (!this.running) {
			switch (type) {
			case 0:
				if (this.hasPre == false) {
					return;
				}
				break;
			case 1:
				if (this.hasNext == false) {
					return;
				}
				break;
			default:

			}

			if (to < 0 || to > this.itemLength - this.v) {
				console.log("can't go");
				return;
			} else {
				console.log("set Curr to" + to);
				this.curr = to;
				this.start = to;

				console.log("after set: ");
				console.log("this.start=" + this.start + " -  " + this.scrollNo);
				console.log("isRunning: " + this.running);
			}

			this.running = true;

			Animate("sliderUL").animate( {
				left : -(this.curr * this.liSize)
			}, this.speed, this.easing, function() {
				//
				});

			this.running = false;

			console.log("this.curr - this.scrollNo:" + (this.curr - this.scrollNo));
			console.log("this.curr + this.scrollNo:" + (this.curr + this.scrollNo) + "  this.itemLength - this.v"
					+ (this.itemLength - this.visible));

			if (this.curr - this.scrollNo < 0) {
				this.hasPre = false;
			} else {
				this.hasPre = true;
			}

			if (this.curr + this.scrollNo > this.itemLength - this.visible) {
				this.hasNext = false;
			} else {
				this.hasNext = true;
			}

		} else {
			console.log("Is Running, no use");
			return false;
		}

	},

	init : function() {
		var running = false;

		var div = $(".imageSlider");
		var ul = $("#sliderUL");
		var tLi = $(".sliderLI");
		var tl = tLi.length;
		var v = this.visible;

		var li = $(".sliderLI");
		this.itemLength = li.length;
		var curr = this.start;
		$(".imageSlider").css("visibility", "visible");

		$(".sliderLI").css( {"overflow" : "hidden","float" : "left"});

		$("#sliderUL").css( {
			margin : "0",
			padding : "0",
			position : "relative",
			"list-style-type" : "none",
			"z-index" : "1"
		});

		$(".imageSlider").css( {
			overflow : "hidden",
			position : "relative",
			"z-index" : "2",
			left : "0px"
		});

		this.liSize = width($(".sliderLI"));
		console.log("this.liSize" + this.liSize);

		var ulSize = this.liSize * this.itemLength;
		var divSize = this.liSize * v;

		$(".sliderLI").css( {
			width : li.width(),
			height : li.height()
		});
		$("#sliderUL").css(this.sizeCss, ulSize + "px");
		$("#sliderUL").css(this.animCss, -(curr * this.liSize));

		$(".imageSlider").css(this.sizeCss, divSize + "px"); // Width of the
	// DIV.
}

};

function getCssValue(el, prop) {
	return parseInt($.css(el[0], prop)) || 0;
}
function width(el) {
	return el[0].offsetWidth + el[0].style.marginLeft + el[0].style.marginRight;
}
function height(el) {
	return el[0].offsetHeight + el[0].style.marginTop + el[0].style.marginBottom;
}

var Animate = function(id) {
	var elem = document.getElementById(id), // 对象
	f = j = 0, callback, _this = {}, // j动画总数
	tween = function(t, b, c, d) {
		return -c * (t /= d) * (t - 2) + b
	}
	// 算子你可以改变他来让你的动画不一样
	_this.execution = function(key, val, t) {
		var s = (new Date()).getTime(), d = t || 500, b = parseInt(elem.style[key]) || 0, c = val - b;
		(function() {
			var t = (new Date()).getTime() - s;
			if (t > d) {
				t = d;
				elem.style[key] = tween(t, b, c, d) + 'px';
				// if(++f==j && callback){callback.apply(elem)}
				++f == j && callback && callback.apply(elem);
				// 这句跟上面注释掉的一句是一个意思，我在google压缩的时候发现了这句
				// 感觉很不错。
				return _this;
			}
			elem.style[key] = tween(t, b, c, d) + 'px';
			setTimeout(arguments.callee, 10);
			// arguments.callee 匿名函数递归调用
		})();
		// 只能写一个这个了。
	}
	_this.animate = function(sty, t, fn) {
		// sty,t,fn 分别为 变化的参数key,val形式,动画用时,回调函数
		callback = fn;
		// 多key 循环设置变化
		for ( var i in sty) {
			j++;// 动画计数器用于判断是否所有动画都完成了。
			_this.execution(i, parseInt(sty[i]), t);
		}
	}
	return _this;
}

var SLIDER = new Slider();
