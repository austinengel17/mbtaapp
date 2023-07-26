import { useState, useEffect, useRef } from "react";
import * as d3 from "d3";
import Draw from'./draw.js';
import Vehicles from'./Vehicles.js';
import LineDropdown from'./LineDropdown.js';
import Select from 'react-select';


function Map() { 
  const svgRef = useRef(null);
  const [stationData, setStationData] = useState(null);
  const [stationMapping, setStationMapping] = useState(null);
  const [selectedLine, setSelectedLine] = useState("Green-E");
  var backendHost = "localhost:8080/";
  var controllerEndpoint = "mbta/v1/livemap";
  var vehiclePositionSub = "/vehicle/location/" + selectedLine;
  var mbtaStopsEndpoint = "/stops/line/" + selectedLine;
  var stationMappingEndpoint = "/stops/child-parent-relation";
  const options = [
    { value: 'Green-E', label: 'Green-E' },
    { value: 'Green-D', label: 'Green-D' },
    { value: 'Orange', label: 'Orange' }
  ];

useEffect(()=>{
////////////////////////////////////////////////////////////
  var xhr = new XMLHttpRequest();
    // Making our connection 
  var url = "http://" + backendHost + controllerEndpoint + mbtaStopsEndpoint;
  xhr.open("GET", url, true);
 
    // function execute after request is successful
  xhr.onreadystatechange = function () {
    console.log("hi");
    if (this.readyState == 4 && this.status == 200) {
        var temp = JSON.parse(this.responseText);
        var stationDataTemp = Object.keys(temp).map(v => ({[v.replace(/['"]+/g, '')]: {...temp[v]}}));
        console.log(stationDataTemp);
        setStationData(stationDataTemp);     
    }
  }
  xhr.onerror = function (error){
      console.error(error);
    };
  xhr.send();
  console.log("sent #1");

///////////////////////////////////////////
  var xhr2 = new XMLHttpRequest();
    // Making our connection 
  var url2 = "http://" + backendHost + controllerEndpoint + stationMappingEndpoint;
  xhr2.open("GET", url2, true);
    // function execute after request is successful
  xhr2.onreadystatechange = function () {
    console.log("hi");
    if (this.readyState == 4 && this.status == 200) {
        var temp = JSON.parse(this.responseText);
        console.log("mapping .. :", temp);
        setStationMapping(temp);      
    }
  }
  xhr2.onerror = function (error){
      console.error(error);
    };
  xhr2.send();
  console.log("sent #2");
/////////////////////////
},[selectedLine]);

  const handleChange = (selectedOption) => {
    setSelectedLine(selectedOption.value);
    console.log(`Option selected:`, selectedOption.value);
  };


  if(stationData == null || stationMapping == null){
    return(<div>Loading...</div>);
  }
  return (
    <div className="Map">
      <svg height="500" width="2000" ref={svgRef} >
        <Draw stationData={stationData} svgRef={svgRef} selectedLine={selectedLine}/>
        <Vehicles svgRef={svgRef} stationData={stationData} stationMapping={stationMapping} selectedLine={selectedLine}/>
      </svg>
      <Select options={options} onChange={handleChange}/>
    </div> 
  );

}

export default Map;
