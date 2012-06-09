function Person() {
}

Person.prototype = {
	constructor : Person,
	name : "Nicholas",
	age : 29,
	job : "dfadf",
	friends : [ "shel", "court" ],
	sayName : function() {
		alert(this.name)
	}
}

var p1 = new Person();
var p2 = new Person();

p1.friends.push("va");

console.log(p1.friends);
console.log(p2.friends);
console.log(p2.friends == p1.friends);

p1.friends = [ "sdafadf", "sdfa" ];

console.log(p1.friends);
console.log(p2.friends);
console.log(p2.friends == p1.friends);

function Obj() {
	this.lastName = "Last Name";
	this.firstName = "First Name";

	for ( var i = 0; i < 100; i++) {
		this['i' + i] = i;
	}

}

Obj.prototype.getName = function() {

	return [ this.lastName, this.firstName ].join("-");

}

var obj = new Obj();

for (var i in obj) {
    if (obj.hasOwnProperty(i)) {
        console.log(i);
    }
}
