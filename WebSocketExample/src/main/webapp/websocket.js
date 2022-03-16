window.onload = init;

//DOM elements

const showFormButton = document.querySelector('.addDevice .button a');
const addButton = document.querySelector('#add_button');
const cancelButton = document.querySelector('#cancel_button');
const addDeviceForm = document.querySelector('.addDeviceForm');
const content = document.querySelector('.content');


//FUNCTIONS
function init() {	
	document.querySelector('.addDeviceForm').style.display = 'none';
}


function createDeviceElement(device) {
	
    const deviceDiv = document.createElement("div");
    deviceDiv.setAttribute("id", device.id);

    const deviceTracking = document.createElement("td");
	deviceTracking.setAttribute("id", "deviceTracking" );
    deviceTracking.innerHTML = (device.currency1 + " ➜ " + device.currency2);
    deviceDiv.appendChild(deviceTracking);

	const devicePrice = document.createElement("td");
    devicePrice.innerHTML = (device.price);
    deviceDiv.appendChild(devicePrice);

	const DatePrice = document.createElement("td");
    DatePrice.innerHTML = (device.date);
    deviceDiv.appendChild(DatePrice);

	const devicePriceMax = document.createElement("td");
    devicePriceMax.innerHTML = (device.price_max);
    deviceDiv.appendChild(devicePriceMax);

	const deviceDateMax = document.createElement("td");
    deviceDateMax.innerHTML = (device.date_max);
    deviceDiv.appendChild(deviceDateMax);

    const removeDevice = document.createElement("td");
    removeDevice.setAttribute("class", "removeDevice");
	removeDevice.innerHTML = "<a href=\"#\" id="+ device.id +" data-op=\"remove\">Remove</a>";
    deviceDiv.appendChild(removeDevice);

    return deviceDiv;
}


function onMessage(event) {
    const device = JSON.parse(event.data);
    if (device.action === "add") {
        const newDeviceElement = createDeviceElement(device);
        content.appendChild(newDeviceElement);
    }
    if (device.action === "remove") {
        document.getElementById(device.id).remove();
    }
	if (device.action === "update") {
        const node = document.getElementById(device.id);

		const statusText = node.children[1];
		statusText.innerHTML = (device.price);
		
		const statusText1 = node.children[2];
		statusText1.innerHTML = (device.date);
		
		const statusText2 = node.children[3];
		statusText2.innerHTML = (device.price_max);
		
		const statusText3 = node.children[4];
		statusText3.innerHTML = (device.date_max);
    }

}



//HANDLERS
function handleShowFormButton() {
	addDeviceForm.style.display = '';
}

function handleAddButton() {
	const currency1 = addDeviceForm.querySelector('#device_type1').value;
	const currency2 = addDeviceForm.querySelector('#device_type2').value;

	addDeviceForm.style.display = 'none';
    addDeviceForm.reset();
    
	const DeviceAction = {
    	action: "add",
		currency1: currency1,
		currency2: currency2
    };
    socket.send(JSON.stringify(DeviceAction));
}


function handleCancelButton() {
	addDeviceForm.style.display = 'none';
    addDeviceForm.reset();
}


//LISTENERS
const socket = new WebSocket("ws://localhost:8081/websocketexample/actions");
socket.onmessage = onMessage;

showFormButton.addEventListener('click', handleShowFormButton);
addButton.addEventListener('click', handleAddButton);
cancelButton.addEventListener('click', handleCancelButton);

content.addEventListener('click', e => {
	
	if(e.target.getAttribute('data-op') === 'remove') {
    	const DeviceAction = {
        	action: "remove",
        	id: parseInt(e.target.id)
    	};
    	socket.send(JSON.stringify(DeviceAction));
    }

});
