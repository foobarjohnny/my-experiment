function AutoComplete(sources, options, callback) {
	// min char to start matching
	this.min_char = options.min_char != null ? options.min_char : 3,
	// max results to show
	this.max_len = options.max_len != null ? options.max_len : 10,
	// suggestion div style
	this.pop_cn = options.pop_css != null ? options.pop_css : '',
	// normal li style
	this.normal = options.li_normal != null ? options.li_normal : '',
	// chosen li style
	this.highlight = options.li_highlight != null ? options.li_highlight : '',
	// data source for matching, sources[i]["msg"]
	this.source = sources;
	// calllback function
	this.callback = callback;
	this.object = null;
	this.previewHighLightItem = null;
};

AutoComplete.prototype = {
	constructor : AutoComplete,

	replaceObjSRC : function(element, oldSRC, newSRC) {
		if (null != element) {
			var currentSRC = element.src;
			eval("var patt=/_" + oldSRC + "\\./ig;");
			var changedSRC = currentSRC.replace(patt, "_" + newSRC + ".");
			element.src = changedSRC;
		}
	},

	init : function() {
		this.setDom();
		return this;
	},

	highlightItem : function(element) {
		if (element) {
			this.disHighlightItem(this.previewHighLightItem);
			element.className = this.highlight;
			this.replaceObjSRC(this.getSuggestImg(element), "unfocused", "focused");
			this.previewHighLightItem = element;
		}
	},

	disHighlightItem : function(element) {
		if (element) {
			element.className = this.normal;
			this.replaceObjSRC(this.getSuggestImg(element), "focused", "unfocused");
		}
	},

	bind : function(obj) {
		if (obj.getAttribute('type') != 'text' || obj.nodeName != 'INPUT') {
			return null;
		} else {
			this.object = obj;
		}

		var self = this;
		obj.onkeyup = function(e) {
			e = e || window.event;
			var lis = self.pop.getElementsByTagName('li'), lens = self.pop.getElementsByTagName('li').length, n = lens, temp;
			if (e.keyCode == 38) { // up key pressed
				CommonUtil.debug("up key pressed========");
				if (self.pop.style.display != 'none') { // pop showing
					for ( var i = 0; i < lens; i++) { // iterator
						if (lis[i].className != self.normal) {
							temp = i;
						} else {
							n--;
						}
					}
					if (n == 0) {
						// choose the last one, if there is no li chose
						lis[lens - 1].className = self.highlight;
						this.value = lis[lens - 1].innerText;
					} else {
						if (lis[temp] == lis[0]) {
							lis[lens - 1].className = self.highlight;
							this.value = lis[lens - 1].innerText;
							lis[temp].className = self.normal;
						} else {
							lis[temp - 1].className = self.highlight;
							this.value = lis[temp - 1].innerText;
							lis[temp].className = self.normal;
						}
					}
				} else {
					self.insert(this, self);
				}
			} else if (e.keyCode == 40) { // down key pressed
				CommonUtil.debug("down key pressed=========");
				if (self.pop.style.display != 'none') {
					for ( var i = 0; i < lens; i++) {
						if (lis[i].className != self.normal) {
							temp = i;
						} else {
							n--;
						}
					}
					if (n == 0) {
						lis[0].className = self.highlight;
						this.value = lis[0].innerText;
					} else {
						if (lis[temp] == lis[lens - 1]) {
							lis[0].className = self.highlight;
							this.value = lis[0].innerText;
							lis[temp].className = self.normal;
						} else {
							lis[temp + 1].className = self.highlight;
							this.value = lis[temp + 1].innerText;
							lis[temp].className = self.normal;
						}
					}
				} else {
					self.insert(this, self);
				}
			} else {
				// neither up key nor down key
				self.insert(this, self);
			}
		};
		obj.onblur = function() {
			CommonUtil.debug("obj.onblur");
			setTimeout(function() {
				self.pop.style.display = 'none';
			}, 3000);
		};
		return this;
	},

	setDom : function() {
		var self = this;
		var dom = document.createElement('div'), ul = document.createElement('ul');
		document.body.appendChild(dom);

		with (dom) { // bind ontouchstart and ontouchend functions to li
			className = this.pop_cn;
			appendChild(ul);
			// If from PC, bind onmouseover and onmouseout functions to li
			// complementally
			if (Global_fromPC == "true") {
				onmouseover = function(e) {
					CommonUtil.debug("onmouseover");
					e = e || window.event;
					var target = self.getSuggestLi(e);
					if (target != null) {
						self.highlightItem(target);
					}
				};
				onmouseout = function(e) {
					CommonUtil.debug("onmouseout");
					e = e || window.event;
					var target = self.getSuggestLi(e);
					if (target != null) {
						self.disHighlightItem(target);
					}
				};
			}
			ontouchstart = function(e) {
				CommonUtil.debug("ontouchstart");
				e = e || window.event;
				var target = self.getSuggestLi(e);
				if (target != null) {
					self.highlightItem(target);
				}
			};
			ontouchend = function(e) {
				CommonUtil.debug("ontouchend");
				var target = self.getSuggestLi(e);
				if (target != null) {
					self.disHighlightItem(target);
				}
			};
		}
		this.pop = dom;
	},

	insert : function(self, autoComplete) {
		var fn_callback = this.callback;
		var object = this.object;
		var jo_bak = [], bak = [], s, li = [], left = 0, top = 0, val = self.value, images = [];
		var sourceLen = this.source.length;
		if (!!val && val.length >= this.min_char) { // min characters
			for ( var i = 0; i < sourceLen; i++) { // match data
				var jo = this.source[i];
				var msg = jo["msg"];
				var source = jo["source"];
				if (msg.toLowerCase().indexOf(val.toLowerCase()) == 0) {
					bak.push(msg);
					jo_bak.push(jo);
					if (!!source) {
						images.push(source + "_unfocused.png");
					} else {
						images.push("");
					}
				}
			}
		}
		if (bak.length == 0) { // hide pop if there is no data matched
			this.pop.style.display = 'none';
			return false;
		}
		// left = self.getBoundingClientRect().left +
		// document.documentElement.scrollLeft;
		// top = self.getBoundingClientRect().top +
		// document.documentElement.scrollTop + self.offsetHeight;
		left = self.offsetLeft;
		top = self.offsetTop + self.offsetHeight;

		with (this.pop) {
			style.cssText = 'width:' + self.offsetWidth + 'px;' + 'position:absolute;left:' + left + 'px;top:' + top + 'px;display:none;';
			onclick = function(e) {
				CommonUtil.debug("onclick==========================");
				e = e || window.event;
				var target = autoComplete.getSuggestLi(e);
				if (target != null) {
					CommonUtil.debug("onclick" + target.innerText);
					var chose_value = target.innerText;
					this.style.display = 'none';
					if (fn_callback) {
						var jo_back_length = jo_bak.length;
						for ( var j = 0; j < jo_back_length; j++) {
							if (chose_value == jo_bak[j]["msg"]) {
								fn_callback(jo_bak[j], object);
							}
						}
					} else {
						self.value = chose_value;
					}
				}
			};
			onresize = function(e) {
				// left = self.getBoundingClientRect().left +
				// document.documentElement.scrollLeft;
				// top = self.getBoundingClientRect().top +
				// document.documentElement.scrollTop + self.offsetHeight;
				left = self.offsetLeft;
				top = self.offsetTop + self.offsetHeight;
				style.cssText = 'width:' + self.offsetWidth + 'px;' + 'position:absolute;left:' + left + 'px;top:' + top + 'px;display:' + style.display + ';';
			};
		}
		s = bak.length > this.max_len ? this.max_len : bak.length;
		for ( var i = 0, imgSrc; i < s; i++) {
			// li.push('<li id="suggestLi">' + bak[i] + '</li>');
			imgSrc = images[i];
			if (!!imgSrc) {
				imgSrc = '<img id="suggestImg" src="' + GLOBAL_imageCommonUrl + imgSrc + '">';
			} else {
				imgSrc = "";
			}

			// li.push('<li id="suggestLi"><div id="suggestImageDiv"
			// class="div_cell">' + imgSrc + '</div><div id="suggestText"
			// class="div_cell suggestTextCss">' + bak[i] + '</div></li>');
			li.push('<li id="suggestLi"><div style="display: table; float:left; height: 100%;"><div id="suggestImageDiv" class="div_cell">' + imgSrc + '</div></div>' + bak[i] + '</li>');
		}

		this.pop.getElementsByTagName('ul')[0].innerHTML = li.join('');
		for ( var j = 0; j < s; j++) {
			this.pop.getElementsByTagName('li')[j].className = this.normal;
		}
		this.pop.style.display = 'block';
		// CommonUtil.debug(this.pop);
	},

	getSuggestLi : function(e) {
		var target = e.srcElement || e.target;
		do {
			if (target.id == "suggestLi") {
				return target;
			}
		} while (target = target.parentNode || target.parentNode);
		return null;
	},

	getSuggestImg : function(e) {
		var x = e.getElementsByTagName('img');
		for ( var i = 0; i < x.length; i++) {
			if (x[i].id == "suggestImg")
				return x[i];
		}
		return null;
	},

	getSuggestText : function(e) {
		var x = e.getElementsByTagName('div');
		for ( var i = 0; i < x.length; i++) {
			if (x[i].id == "suggestText")
				return x[i];
		}
		return null;
	}
};
