import { useState, useEffect } from "react";
import * as d3 from "d3";

  
  function Draw({ svgRef, stationData, selectedLine}) {
  var lineYAxis = 150;
  var circleRadius = 2;
  console.log("st data .. ", stationData);
  useEffect(()=>{

    var svg = d3.select(svgRef.current);
    var stopGroup = svg.selectAll("g")
    .data(stationData)
    .enter()
    .append("g")
    .attr("transform", function(d, i) {
      return "translate(" + (30 + i * 60) + ", " + (lineYAxis) + ")";
    });
    stopGroup.append("circle")
    .attr("cx", 0)
    .attr("cy", 0)
    .attr("r", circleRadius)
    .attr("fill", "green")
    .attr("id", d=>Object.keys(d)[0])
    .attr("class", "station");

    stopGroup.append("text")
    .text(function(d) {
      return d[Object.keys(d)[0]].name;
    })
    .attr("width", 100)
    .attr("x", 0) // Set the x-coordinate
    .attr("y", -10)
    .attr("text-anchor", "middle")
    .attr("font-size", 5);
    var stops = svg.selectAll("circle").nodes();

    for(var i = 0; i < stops.length-1; i++){
    var node1 = stops[i];
    var node2 = stops[i+1];
    var circlePosition1 = stops[i].getBoundingClientRect();
    var circlePosition2 = stops[i+1].getBoundingClientRect();

    svg.append("line")
    .attr("x1", +circlePosition1.left + window.pageXOffset + circleRadius*2)
    .attr("y1", 150)
    .attr("x2", +circlePosition2.left + window.pageXOffset)
    .attr("y2", 150)
    .attr("stroke", "green")
    .attr("stroke-width", 1);

    console.log("Circle absolute position:", node1.r);
    console.log(stops[i]);
    }
   },[stationData]);  
 
}

export default Draw;
