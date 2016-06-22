"use strict";

angular.module('de.isnow.filters', []);

console.log ("filters inited");

angular.module('de.isnow.filters')
.filter('ellipsisDisplay', function () {
	return function(str, len) {
		len = len || 23;
		if (str && (str.length > len)) {
			return str.substring(0, len-4)+'...';
		}
		return str;
	};
}
);

angular.module('de.isnow.filters')
.filter('capitalize', function () {
	return function(str, len) {
		if (str) {
			return str.substring(0,1).toUpperCase()+str.substring(1, str.length);
		}
	};
}
);

angular.module('de.isnow.filters')
.filter('orderObjectBy', function() {
	return function(items, field, reverse) {
		var filtered = [];
		angular.forEach(items, function(item) {
			filtered.push(item);
		});
		filtered.sort(function (a, b) {
			return (a[field] > b[field] ? 1 : -1);
		});
		if(reverse) filtered.reverse();
		return filtered;
	};
});

/*
 * This filter allows an array to be printed as a
 * semicolon delimited string
 */

angular.module('de.isnow.filters')
.filter('userknowledgeCompoundNames', function() {
	return function(dataArray) {
		if (typeof dataArray == 'string') {
			return dataArray;
		}
	
		if (dataArray) {
			var str = [];
			for (var i in dataArray) {
				if(dataArray[i].mtxCompound) {
					str.push(dataArray[i].mtxCompound.name);
				}
			}
			return str.join('; ');
		}
	}
});

/*
 * This filter allows an array to be printed as a
 * semicolon delimited string
 */

angular.module('de.isnow.filters')
.filter('arrayDisplay', function() {
	return function(dataArray, propName) {
		if (typeof dataArray == 'string') {
			return dataArray;
		}
	
		if (dataArray) {
			if (propName) {
				var str = [];
				for (var i in dataArray) {
					if(dataArray[i][propName]) {
						str.push(dataArray[i][propName]);
					}
				}
				return str.join('; ');
			} else {
				return dataArray.join('; ');
			} 
		}
	}
});
