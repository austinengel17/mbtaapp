import { useState, useEffect } from "react";
import * as d3 from "d3";
import './Vehicles.css';


function Vehicles({ svgRef, stationData, stationMapping, selectedLine}) { 
    const [vehicleList, setVehicleList] = useState([]);
    var backendHost = "localhost:8080/";
    var controllerEndpoint = "mbta/v1/livemap";
    var vehiclePositionSub = "/vehicle/location/" + selectedLine;
    var mbtaStopsEndpoint = "/stops/line/" + selectedLine;
    var inTransitTo = "IN_TRANSIT_TO";
    var incomingAt = "INCOMING_AT";


    useEffect(() => {
      const svg = svgRef.current;         
      var svgElement = d3.select(svg);    //left facing arrow marker

      svgElement.selectAll("g.vehicle-group")
      .data(vehicleList, d=>d.id)
      .join(
        (enter) => {
          var vGroup = enter.append("g")
          .attr("class", "vehicle-group")
          .attr("transform", function(d, i){
            var stopId = d.stopId;
              var stopName = stationMapping[stopId];
              console.log('pre bounding rec client ', stopName);
              console.log('pre bounding rec client ', stopId);
              var station = svgElement.selectAll("#" + stopName)
              var circlePosition = station.node().getBoundingClientRect()
              var x = +circlePosition.left + window.pageXOffset;
              var directionId = d.directionId;
              if(d.currentStatus == inTransitTo){
                if(directionId == 0)
                  x=x+30;
                else
                  x=x-30;
              }
              if(d.currentStatus == incomingAt){
                if(directionId == 0)
                  x=x+15;
                else
                  x=x-15;
              }

            return "translate(" + x + " , 155)";
          });

           // vGroup.append("rect")
           //  .attr("x", 0)
           //  .attr("y", 0)
           //  .attr("width", 5)
           //  .attr("height", 5);

           vGroup.append("polygon") 
            .attr("points", function(d){
              if(d.directionId == 1)
                return "3,0 -3,0 -3,7 3,7 7,3"; //outbound
              else
                return "-3,5 3,5 3,12 -3,12 -7,8"; //inbound
            })
            .attr("fill", "lightgrey");

          vGroup
          .append("text")
          .attr("class", "vehicle")
          .attr("x", 15)
          .attr("y",0)
          .attr("font-size", 5)
          .attr("transform", "rotate(45)")
          .text(function(d){
             var stopId = d.stopId;
            var stopName = stationMapping[stopId];
            return d.id + " - " + d.currentStatus + ' - ' + stationMapping[stopId] + ' - direction: ' + d.directionId;
          });
      },
      (update) => {
        update
        .transition()
        .duration(3000)
        .attr("transform", function(d, i){
            var stopId = d.stopId;
              var stopName = stationMapping[stopId];
              var station = svgElement.selectAll("#" + stopName)
              console.log('pre bounding rec client ', stopName);
              console.log('pre bounding rec client ', stopId);
              var circlePosition = station.node().getBoundingClientRect()
              var x = +circlePosition.left + window.pageXOffset;
              var directionId = d.directionId;
              if(d.currentStatus == inTransitTo){
                if(directionId == 0)
                  x=x+30;
                else
                  x=x-30;
              }
              if(d.currentStatus == incomingAt){
                if(directionId == 0)
                  x=x+15;
                else
                  x=x-15;
              }

            return "translate(" + x + " , 155)";
          });
        update.select("polygon")
          .attr("points", function(d){
              if(d.directionId == 1)
                return "3,0 -3,0 -3,7 3,7 7,3"; //outbound
              else
                return "-3,5 3,5 3,12 -3,12 -7,8"; //inbound
            })
        update.select("text").text(function(d){
          var stopId = d.stopId;
          var stopName = stationMapping[stopId];
          return d.id + " - " + d.currentStatus + ' - ' + stationMapping[stopId] + ' - direction: ' + d.directionId;// == 0 ? "inbound" : "outbound";
        });
      },
      (exit) => {
        exit.remove(); 
    } 
      );
    }, [vehicleList]);

    //Load vehicles below
    useEffect(() => {
      const evtSource = new EventSource("http://" + backendHost + controllerEndpoint + vehiclePositionSub);
      evtSource.onmessage = (data) => {
        var eventData = JSON.parse(data.data);
        console.log("Event data .. ", eventData);
        var vehicleData = eventData.vehicleData;
        if(eventData.event == "reset"){
          console.log("reset");
          setVehicleList(vehicleData);
        } else if(eventData.event == "update"){
          setVehicleList((prevList) => {
          var newData = prevList.map((item) => {
            if (item.id === vehicleData[0].id && !(item.currentStatus === vehicleData[0].currentStatus)) {
              return vehicleData[0];
            }
            return item;
          });
          console.log("update", newData);
          return newData;
        });
        }

      };
      evtSource.onerror = function (error) {
        console.error('EventSource error:', error);
      };
      //close out event subscription on close of component
      return () => {
        evtSource.close();
      };
    },[selectedLine]);
}



export default Vehicles;
