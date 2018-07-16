(function() {
	/*var firebaseRD = require('firebase-admin');
	var firebaseGCM = require('firebase-admin');
	//export GCLOUD_PROJECT='takemyorder-8a08a'

	var configRD = {
		apiKey: "AIzaSyAiqEVvAp46pif7wxRfbzfjjKs7Sj2n7Aw",
		authDomain: "takemyorder-8a08a.firebaseapp.com",
		databaseURL: "https://takemyorder-8a08a.firebaseio.com",
		storageBucket: "takemyorder-8a08a.appspot.com",
		messagingSenderId: "553847180557",
		projectId: "takemyorder-8a08a"
	};
	firebaseRD.initializeApp(configRD);
	
	var configGCM = {
		apiKey: "AIzaSyATEte9wXqvzyLwyR7ke2MB340z415YsBo",
		projectId: "takemyorder-8a08a"
	};
	firebaseGCM.initializeApp(configGCM, "GCM");*/

	//var database = firebaseRD.database();	
	
	var admin = require("firebase-admin");

	var serviceAccount = require("./takemyorder-8a08a-firebase-adminsdk-nl2ry-28d98a4a7e.json");

	admin.initializeApp({
	  credential: admin.credential.cert(serviceAccount),
	  databaseURL: "https://takemyorder-8a08a.firebaseio.com"
	});

 
	var restaurantListCalls = {};
	var starCountRef = admin.database().ref().child('waiters_calls');
	starCountRef.on('value', snap => {
		snap.forEach(function(child) {
			console.log(child.key+': '+child.val() + ' Val Size: ' + Object.keys(child.val()).length);
			
			if(Object.keys(child.val()).length > restaurantListCalls[child.key]) {
				console.log("New waiter call on " + child.key);			
				var call = {};
				child.forEach(function(subChild) {
					console.log(subChild.val().tableId);
					call = subChild.val();
				});
				
				var topic = "calls_" + child.key;
				
				console.log("Sent to TOPIC: " + topic);
				
				var message = {
				  notification: {
					'body': 'New waiter call from table ' + call.tableId,
					'title': 'New waiter call'
				  },
				  android: {
					data: {'restaurantId': child.key },
				  },
				  topic: topic
				};
				/*var message = {
				  data: {
					message: 'New waiter call from table ' + call.tableId
				  },
				  topic: topic
				};*/

				// Send a message to devices subscribed to the provided topic.
				admin.messaging().send(message)
				  .then((response) => {
					// Response is a message ID string.
					console.log('Successfully sent message:', response);
				  })
				  .catch((error) => {
					console.log('Error sending message:', error);
				  });

			}
			
			restaurantListCalls[child.key] = Object.keys(child.val()).length;			
		});
	});
	
	
	function sendNotificationToUser(restaurantId, message, onSuccess) {
	  request({
		url: 'https://fcm.googleapis.com/fcm/send',
		method: 'POST',
		headers: {
		  'Content-Type' :' application/json',
		  'Authorization': 'key=AIzaSyATEte9wXqvzyLwyR7ke2MB340z415YsBo'
		},
		body: JSON.stringify({
		  notification: {
			title: message
		  },
		  to : '/topics/calls_'+restaurantId
		})
	  }, function(error, response, body) {
		if (error) { console.error(error); }
		else if (response.statusCode >= 400) { 
		  console.error('HTTP Error: '+response.statusCode+' - '+response.statusMessage); 
		}
		else {
		  //onSuccess();
		  console.log('SENT');
		}
	  });
	}

	
}());