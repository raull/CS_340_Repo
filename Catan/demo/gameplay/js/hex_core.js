// STUDENT-CORE-BEGIN
// DO NOT EDIT THIS FILE
var catan = catan || {};

	/**
		This is the namespace for what abstracts the hex addressing scheme:
			The location classes and direction constants.
		@module catan.hex_core
		@namespace hex_core
	*/

catan.hex_core = (function HexCore_Namespace(){
	
	
	function positiveModulo(lhs,rhs){
		return ((lhs % rhs) + rhs) % rhs;
	}
	
	function getOppositeDirection(direction){
		return positiveModulo((direction + 3),6);
	}
	
	//Works on Hex, Edge and Vertex Directions
	function nextDirectionClockwise(direction){
		return positiveModulo((direction + 1),6);
	}
	
	//Works on Hex, Edge and Vertex Directions
	function nextDirectionCounterClockwise(direction){
		return positiveModulo((direction - 1),6);
	}
	
	/**
    Use this class to look up the numerical value of a EdgeDirection from JSON.
	These are the edge values in clockwise order starting at NW.
	They are in order so that modulo math makes this easy
	Edge and Vertex Directions give you the edge and then the vertex in clockwise order
    It's really just an enumeration. For example EdgeDirection["NW"] or EdgeDirection.NW returns 0;
    The possible edge directions are "NW","N","NE","SE","S","SW"
	
	@class EdgeDirection
	*/
    var edLookup = ["NW","N","NE","SE","S","SW"]
	var EdgeDirection = core.numberEnumeration(edLookup);
    
	/**
	These are simply a copy of EdgeDirections. They can be fed to a hex_core.HexLocation to get 
	the location of the hex next to it in that direction. It's really just an enumeration.
	@class HexDirection
	*/
	var HexDirection = EdgeDirection;
	/**
    Use this class to look up the numerical value of a VertexDirection from JSON.
	These are the vertex values in clockwise order starting at NW.
	They are in order so that modulo math makes this easy
	Edge and Vertex Directions give you the edge and then the vertex in clockwise order.
    It's really just an enumeration. For example VertexDirection["NW"] or VertexDirection.NW returns 1;
    The possible VertexDirection are "W","NW","NE","E","SE","SW"
	@class VertexDirection
	*/
    var vdLookup = ["W","NW","NE","E","SE","SW"]
	var VertexDirection = core.numberEnumeration(vdLookup);
    

	/**
	  This represents a location of a hex in a hex coordinate system. It has some utility functions
	 
        @class HexLocation
		@constructor
		@param {Integer} x
		@param {Integer} y
	*/
	var HexLocation = (function HexLocationClass(){

		function HexLocation(x,y){
			this.x = x;
			this.y = y;
		}
		
		core.defineProperty(HexLocation.prototype,"x");
		core.defineProperty(HexLocation.prototype,"y");		
		
        /**
          This is used to check if two locations are equal
         
            @method equals
            @param {hex_core.HexLocation} otherLocation
            @return boolean Returns true if the other location has the same x,y
        */
		HexLocation.prototype.equals =  function(otherLocation){
			return (otherLocation && this.getX() == otherLocation.getX() && otherLocation.getY() == this.getY()); 
		}
        
        HexLocation.prototype.getUID = function(){
            return "H"+this.x+","+this.y;
        }
		
        /**
          This gets the surrounding hex locations
         
            @method getNeighborLocation
            @param {[HexDirection]} hexDirection
            @return hex_core.HexLocation Returns a location next to this one, in the direction of the 'hexDirection' given
        */
		HexLocation.prototype.getNeighborLocation = function getNeighborLocation(hexDirection){
			var x,y,z; // z is unused, but the three always sum to 0
		    switch (hexDirection) {
				case HexDirection.SE:
					x = 1; y = 0; z = -1;
					break;
				case HexDirection.S:
					x = 0; y = 1; z = -1;
					break;
				case HexDirection.SW:
					x = -1; y = 1; z = 0;
					break;
				case HexDirection.NW:
					x = -1; y = 0; z = 1;
					break;
				case HexDirection.N:
					x = 0; y = -1; z = 1;
					break;
				case HexDirection.NE:
					x = 1; y = -1; z = 0;
					break;
				default:
					console.log(hexDirection,this);
					core.assert(false);
					throw Error("Invalid Direction "+ hexDirection);
			}
			return new HexLocation(this.x + x,this.y + y);
		}
        
        HexLocation.prototype.getVertexLocations = function(){
            return getLocations(this, VertexLocation);
        }
        
        HexLocation.prototype.getEdgeLocations = function(){
            return getLocations(this, EdgeLocation);
        }
        
        function getLocations(loc, constructor){
            return [0,1,2,3,4,5].map(function(num){ return new constructor(loc,num)});
        }
		
		return HexLocation;
	}());
	
	/**
		This is the base class for VertexLocation and EdgeLocation
        * 
        * -- This is NOT instantiable by itself --
        * 
		
		@class BaseLocation
        @extends hex_core.HexLocation 
        @constructor 
		*/
	var BaseLocation = (function BaseLocationClass(){
		
		BaseLocation.prototype = core.inherit(HexLocation.prototype)
		function BaseLocation(arg1,arg2,arg3){
			var x,y,direction;
            if (arg3 !== undefined){
				x = arg1;
				y = arg2;
				direction = arg3;
            } else if (arg1 === undefined) {
                x = 0;
                y = 0;
                direction = 0;  
            } else {
				x = arg1.x;
				y = arg1.y;
                if (arg2 !== undefined){
                    direction = arg2
                } else{ 
                    direction = arg1.direction
                } 
            }
			this.direction = direction;
			this.x = x;
			this.y = y;
		}
        
		core.defineProperty(BaseLocation.prototype,"direction");
		
        /**
			Returns a reference to the hex that this location is on.
			@method rotateAboutHexCW
			@return {hex_core.HexLocation} The hex this is referencing
			*/
		BaseLocation.prototype.getHexLocation = function(){
			return new HexLocation(this.x,this.y);
		}
        
        BaseLocation.prototype.equals = function(otherLocation){
			if (otherLocation){
                var his = otherLocation.getCanonical();
                var my = this.getCanonical();
                return (my.x == his.x && my.y == his.y && my.direction == his.direction)
            } else {
                return false;
            }
		}
		
		/**
			Get the next logical location by traveling about the center of the hex in a clockwise direction
			@method rotateAboutHexCW
			@return {hex_core.BaseLocation} The next location CW
			*/
		BaseLocation.prototype.rotateAboutHexCW = function(){
			return new this.constructor(
				this.getHexLocation(),
				nextDirectionClockwise(this.getDirection()));
		}
		
		/**
			Get the next logical location by traveling about the center of the hex in a counter-clockwise direction
			
			@method rotateAboutHexCCW
			@return {hex_core.BaseLocation} [Array] The next location CCW
			*/
		BaseLocation.prototype.rotateAboutHexCCW = function(){
			return new this.constructor(
				this.getHexLocation(),
				nextDirectionCounterClockwise(this.getDirection()));
		}
		
		/** 
			Edge and VertexLocation's have class-specific implementations
			
			This function should return all Locations that refer to this spot on the map
			
			@method getEquivalenceGroup
			@return {hex_core.BaseLocation} [Array] A list of equivalent locations including this one
		*/
		BaseLocation.prototype.getEquivalenceGroup = core.abstractMethod;
		
		/**
			Abstract function to be overridden in child-class
			
			@method getNeighborVertexes
			@return {hex_core.VertexLocation} [Array] vertexes touching this location
			*/
		BaseLocation.prototype.getNeighborVertexes = core.abstractMethod;
		
        /**
        Abstract
        Returns a canonical representation of this location (ie unique and consistent - all locations that are equal have the same canonical representation.)
        
        @method getCanonical
        @return {hex_core.BaseLocation} The canonical representation of this location
        */
		BaseLocation.prototype.getCanonical = core.abstractMethod;
		
        
		/**
			Abstract function to be overridden in child-class
			
			@method getConnectedEdges
			@return {hex_core.VertexLocation} [Array] edges touching this locaiton
			*/
		BaseLocation.prototype.getConnectedEdges = core.abstractMethod;
		BaseLocation.prototype.forCommand = function(){
            console.log(this.direction,this.lookup);
            return new this.constructor(this.x,this.y,this.lookup[this.direction]);
        }
        
        /**
        Abstract
		Returns a unique string id for this location
        All locations in any equivalence group will have the same id string.  
		
        @method getIDString
		@return {String}
		*/
        BaseLocation.prototype.getIDString = core.abstractMethod;
		
		return BaseLocation;
	}());
	
	/**
	This class represent an edge location. It consists of a hex location
	and an edge direction
    This takes a hex location and a direction. Alternatively you can use
     (x,y,direction)
    
    @constructor
    @param {hexLocation} hexLocation The location of the parent hex
    @param {hex_core.EdgeDirection} direction The direction of the edge relative to the hex
    @extends hex_core.BaseLocation
	
	@class EdgeLocation
	*/
	var EdgeLocation = (function EdgeLocationClass(){

		core.forceClassInherit(EdgeLocation,BaseLocation);
		function EdgeLocation(baseLoc_hexLocation_x,undef_dir_y,undef_dir){
			BaseLocation.call(this,
                                baseLoc_hexLocation_x,
                                  undef_dir_y,
                                  undef_dir);
		};
		
		EdgeLocation.prototype.extend = function(data){
			data.direction = EdgeDirection[data.direction];
			return $.extend(this,data);
		}
		/**
		This function returns this Edge location as well as the edge location for
		the other hex that this edge touches. 
		
        @method getEquivalenceGroup
		@return {hex_core.EdgeLocation} [Array] The list of all edge locations that this 
		object is equivalent to (reflexive). Size = 2.
		*/
		EdgeLocation.prototype.getEquivalenceGroup = function(){
			var currentLocation = this;
			var otherHexDirection = this.getNeighborLocation(this.getDirection());
			var otherDirection = getOppositeDirection(this.getDirection());
			var otherObject = new EdgeLocation(otherHexDirection,otherDirection);
			return [currentLocation,otherObject];
		}
        
        EdgeLocation.prototype.getCanonical = function getCanonical(){
			var dx,dy,dir;
			switch(this.getDirection()){
				case EdgeDirection.NW:
					dx = 0; dy = 0; dir = EdgeDirection.NW;
					break;
				case EdgeDirection.N:
					dx = 0 ; dy = -1; dir = EdgeDirection.S;
					break;
				case EdgeDirection.NE:
					dx = 0; dy = 0; dir = EdgeDirection.NE;
					break;
				case EdgeDirection.SE:
					dx = 1; dy = 0; dir = EdgeDirection.NW;
					break;
				case EdgeDirection.S:
					dx = 0; dy = 0; dir = EdgeDirection.S;
					break;
				case EdgeDirection.SW:
					dx = -1; dy = 1; dir = EdgeDirection.NE;
					break;
				default:
					throw Error("Illegal edge direction");
			}
			return new EdgeLocation(this.x+dx,this.y+dy,dir);				
        }
		
		EdgeLocation.prototype.getIDString = function(){
			var edge = this.getCanonical();
            return "E:"+edge.x + "," + edge.y +":"+ edge.direction;
		}	
			
		EdgeLocation.prototype.getNeighborVertexes = getNeighborVertexes;
		EdgeLocation.prototype.getConnected = getNeighborVertexes;
		
		function getNeighborVertexes(){
			var vertexDirections = [];
            // works because the edge and vertex directions are both just integer
			var e1 = this.rotateAboutHexCW(); 
			var e2 = this;
	
			vertexDirections.push(new VertexLocation(e2.x, e2.y,e2.direction));
			vertexDirections.push(new VertexLocation(e1.x, e1.y,e1.direction));
			return vertexDirections;
		}
		
		EdgeLocation.prototype.getConnectedEdges = function(){
			var edgeLocations = [];
			this.getEquivalenceGroup().map(function getEdges(edgeLocation){
                edgeLocations.push(edgeLocation.rotateAboutHexCCW());
                edgeLocations.push(edgeLocation.rotateAboutHexCW());
            });
			return edgeLocations;
		}
        
        EdgeLocation.prototype.lookup = edLookup;
		
		return EdgeLocation;
	}());
	/**
	This class represents a vertex location. It consists of a hex location
	and a vertex direction. Instead of (hexLocation, direction) you can use
    (x, y, direction).
    @constructor
    @param {hex_core.HexLocation} hexLocation The location of the parent hex
    @param {hex_core.VertexDirection} direction The direction of the vertex relative to the hex
	@class VertexLocation
	*/
	var VertexLocation = (function VertexLocationClass(){
		core.forceClassInherit(VertexLocation, BaseLocation);
		
		function VertexLocation(hexLocation,direction,arg3){
			BaseLocation.call(this,hexLocation,direction,arg3);
		};
		
		VertexLocation.prototype.extend = function(data){
			data.direction = VertexDirection[data.direction];
			return $.extend(this,data);
		}
	
		/**
		This function returns this VertexLocation as well as the VertexLocation for
		the other 2 hexes that this VertexLocation touches. 
		
        @method getEquivalenceGroup
		@return {hex_core.VertexLocation} [Array] The list of all VertexLocation that this 
		object is equivalent to (reflexive). Size = 3.
		*/
		
		VertexLocation.prototype.getEquivalenceGroup = function(){
			var group = [this,
					new VertexLocation(this.getHexLocation().getNeighborLocation(positiveModulo(this.getDirection()-1,6)),
										positiveModulo(this.getDirection()+2,6)),
					new VertexLocation(this.getHexLocation().getNeighborLocation(this.getDirection()),
										positiveModulo(this.getDirection()+4,6))];
			return group;					
		}

		VertexLocation.prototype.getCanonical = function convertVLtoView(){
            var dx,dy,dir
            switch(this.getDirection()){
                case VertexDirection.W:
                    dx = 0;	 dy = 0;  dir = VertexDirection.W;
                    break;
                case VertexDirection.NW:
                    dx = -1; dy = 0;  dir = VertexDirection.E;
                    break;
                case VertexDirection.NE:
                    dx = 1;	 dy = -1; dir = VertexDirection.W;
                    break;
                case VertexDirection.E:
                    dx = 0;	 dy = 0;  dir = VertexDirection.E;
                    break;
                case VertexDirection.SE:
                    dx = 1;  dy = 0;  dir = VertexDirection.W;
                    break;
                case VertexDirection.SW:
                    dx = -1; dy = 1;  dir = VertexDirection.E;
                    break;
                default:
                    console.log(this);
                    core.assert(false);
            }
            return new VertexLocation(this.x+dx,this.y+dy,dir);			
        }
        
        VertexLocation.prototype.getIDString = function(){
			var vertex = this.getCanonical();
            return "V:"+vertex.x + "," + vertex.y + ":"+vertex.direction;
		}
		
		function getConnectedEdges(){
			var edgeLocations = [];
            this.getEquivalenceGroup().map( function getEdge(vLocation){
                edgeLocations.push(new EdgeLocation(vLocation.getHexLocation(),vLocation.getDirection()));
            });
			return edgeLocations;
		}
		
        /**
		Returns the three edge locations connected to this vertex location
		
        @method getConnectedEdges
		@return {hex_core.EdgeLocation} [Array]
		*/
		VertexLocation.prototype.getConnectedEdges = getConnectedEdges;
		
        /**
		An alias for '.getConnectedEdges' - you could use it in tree traversal?
        @method getConnectedEdges
		@return {hex_core.EdgeLocation} [Array]
		*/
		VertexLocation.prototype.getConnected = getConnectedEdges;
        
        VertexLocation.prototype.lookup = vdLookup; 
        
		return VertexLocation;
	}());
    
	return {
		HexLocation:HexLocation,
		EdgeLocation:EdgeLocation,
		VertexLocation:VertexLocation,
		HexDirection:HexDirection,
		VertexDirection:VertexDirection,
		EdgeDirection:EdgeDirection
	}
}());



		

