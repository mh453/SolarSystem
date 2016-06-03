/*
Scene Graph
- objRoot 
	- mainTG
			- sunBG 	
				- sunTransformGroup1 -(sunTransform1 Scaling)	
					- colourSwitch - Collision detection on sun when UFO collides with sun it changes colour
						- redSun
						- greenSun
						- blueSun
		
			- mercuryBG 
				- mercuryTransformGroup0 - (mercurySunTranform3D0 Sun Rotation) 
					- mercuryTransformGroup1 - (mercuryTranform3D1 Scaling & Translation)
						- mecuryTransformGroup2 - (mecuryTransform3D2 - Rotation on Y Axis) 
							- Planet
			- venusBG
				- venusTransformGroup0 - (venusSunTranform3D0 Sun Rotation) 
					- venusTransformGroup1 - (venusTranform3D1 Scaling & Translation)
						- venusTransformGroup2 - (venusTransform3D2 - Rotation on Y Axis) 
							- Planet
			- earthBG
				- earthTransformGroup0 - (earthSunTranform3D0 Sun Rotation) 
					- earthTransformGroup1 - (earthTranform3D1 Scaling & Translation)
					 	- earthTransformGroup2 - (earthTransform3D2 - Rotation on Y Axis) 
							- Planet		
			- marsBG
				- marsTransformGroup0 - (marsSunTranform3D0 Sun Rotation) 
					- marsTransformGroup1 - (marsTranform3D1 Scaling & Translation)
					 	- marsTransformGroup2 - (marsTransform3D2 - Rotation on Y Axis) 
							- Planet										
			- jupitorBG
				- jupitorTransformGroup0 - (jupitorSunTranform3D0 Sun Rotation) 
					- jupitorTransformGroup1 - (jupitorTranform3D1 Scaling & Translation)
					 	- jupitorTransformGroup2 - (jupitorTransform3D2 - Rotation on Y Axis) 
							- Planet	
			- saturnBG
				- saturnTransformGroup0 - (saturnSunTranform3D0 Sun Rotation) 
					- saturnTransformGroup1 - (saturnTranform3D1 Scaling & Translation)
					 	- saturnTransformGroup2 - (saturnTransform3D2 - Rotation on Y Axis) 
							- Planet				
			- uranusBG
				- uranusTransformGroup0 - (uranusSunTranform3D0 Sun Rotation) 
					- uranusTransformGroup1 - (uranusTranform3D1 Scaling & Translation)
					 	- uranusTransformGroup2 - (uranusTransform3D2 - Rotation on Y Axis) 
							- Planet	
			- neptuneBG
				- neptuneTransformGroup0 - (neptuneSunTranform3D0 Sun Rotation) 
					- neptuneTransformGroup1 - (neptuneTranform3D1 Scaling & Translation)
					 	- neptuneTransformGroup2 - (neptuneTransform3D2 - Rotation on Y Axis) 
							- Planet	

			- plutoBG
				- plutoTransformGroup0 - (plutoSunTranform3D0 Sun Rotation) 
					- plutoTransformGroup1 - (plutoTranform3D1 Scaling & Translation)
					 	- plutoTransformGroup2 - (plutoTransform3D2 - Rotation on Y Axis) 
							- Planet
			- rocketBG
				- rocketTransformGroup0
					- rocketTG1 
						- rocketShell - Rocket Cylinder
						- wingTG1
							- wing1 - Wing are made of two pieces of thin rectangles positioned in x shape
						- wingTG3
							- wing3
						- rocketTG2
							-rocketNose - Rocket Nose add to top of rocket shell
					- RotatorRocket - Make rocket rotate around sun 
					
										
		-	UFOTransformGroup0 - UFO can be moved around when it touches it activates wake up
				- UFO
					- UFOringTransformGroup0
						- UFOring
				
	Most of the planet have the same structure, it has its own branchGroup (xBG), and 3 Transform Group (xTransformGroupx). 
	Transform Group0 is for the rotation around the sun. I have created a method (setRotationAroundSun) which added the rotation to the Transform Group.
	Transform Group1 is for scaling and Translation from the origin (sun). Transform Group 2 is for rotation on the Y Axis, I have again creates a factory method. 
	
	I have added collision detection on the sun, when the UFO (silver sphere and cyclinder at top right position) touches the sun it changes to from red to blue. 
	Also a rocket travels around the sun. 
					
*/

import java.awt.BorderLayout;
import java.awt.Container;

import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Switch;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JApplet;
import javax.vecmath.Color3f;
import javax.vecmath.Color4f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.picking.behaviors.PickTranslateBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class SolarSystem extends JApplet {

	BoundingSphere bounds;
	RotationInterpolator Rotator0;
	RotationInterpolator Rotator1;
	Canvas3D c;

	public SolarSystem() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		c = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		cp.add("Center", c);
		BranchGroup scene = buildSceneGraph();
		SimpleUniverse u = new SimpleUniverse(c);
		u.getViewingPlatform().setNominalViewingTransform();
		u.addBranchGraph(scene);

		// *** create a viewing platform
		TransformGroup cameraTG = u.getViewingPlatform()
				.getViewPlatformTransform();
		// starting postion of the viewing platform
		Vector3f translate = new Vector3f();
		Transform3D T3D = new Transform3D();
		// move along z axis by 10.0f ("move away from the screen")
		translate.set(0.0f, 0.0f, 19.0f);
		T3D.setTranslation(translate);
		cameraTG.setTransform(T3D);
	}

	public BranchGroup buildSceneGraph() {
		// creating the bounds of the universe
		// see mouse behaviour below
		bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10.0);

		// creating the (single) branch group
		BranchGroup objRoot = new BranchGroup();

		// creating the transform group for the (one) branchgroup
		TransformGroup mainTG = new TransformGroup();
		mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		mainTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

		// branch groups of the different elements
		BranchGroup sunBG = new BranchGroup();
		BranchGroup mercuryBG = new BranchGroup();
		BranchGroup venusBG = new BranchGroup();
		BranchGroup earthBG = new BranchGroup();
		BranchGroup marsBG = new BranchGroup();

		BranchGroup jupitorBG = new BranchGroup();
		BranchGroup saturnBG = new BranchGroup();
		BranchGroup uranusBG = new BranchGroup();
		BranchGroup neptuneBG = new BranchGroup();
		BranchGroup plutoBG = new BranchGroup();
		
		BranchGroup rocketBG = new BranchGroup();
		BranchGroup UFOBG = new BranchGroup();
		
		// --------------------- SUN -----------------------------------------

		// scaling on the sun 
		Transform3D sunTransform1 = new Transform3D();
		sunTransform1.setScale(new Vector3d(1.0, 1.0, 1.0));
		TransformGroup sunTransformGroup1 = new TransformGroup(sunTransform1);

		// adding light to sun
		Color3f ambientColourSun = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f emissiveColourSun = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f diffuseColourSun = new Color3f(0.8f, 0.4f, 0.0f);
		Color3f specularColourSun = new Color3f(0.8f, 0.8f, 0.0f);
		float shininessSun = 100.0f;
	
		// adding a texture to the sun, give it a fire texture
		TextureLoader loader = new TextureLoader("Images\\sunBack.jpg","LUMINANCE", new Container());
		Texture texture = loader.getTexture();
		texture.setBoundaryModeS(Texture.WRAP);
		texture.setBoundaryModeT(Texture.WRAP);
		texture.setBoundaryColor(new Color4f(0.0f,1.0f,0.0f,0.0f));
		
		// setting up texture attribute
		TextureAttributes textAttr = new TextureAttributes();
		textAttr.setTextureMode(TextureAttributes.MODULATE);
		
		// combining the texture and lighting
		Appearance ap = new Appearance();
		ap.setTexture(texture);
		ap.setTextureAttributes(textAttr);
		
		// setting up material to give it a glow
		ap.setMaterial(new Material(ambientColourSun,
				emissiveColourSun, diffuseColourSun,
				specularColourSun, shininessSun));
		
		int primflags = Primitive.GENERATE_NORMALS + Primitive.GENERATE_TEXTURE_COORDS;
		
		// red Sun
		Sphere sun = new Sphere(1.0f,primflags,550,ap);	
				
		// ----------------------------- COLOR SWITCH FOR SUN ----------------------------------------
		
		// Using a colour switch so when the UFO touches the sun on entry and exit it will turn to a different colour
		
		// creating lighting and red sphere (sun)
	    Color3f ambientColourRSphere = new Color3f(0.1f,0.8f,0.0f);
	    Color3f emissiveColourRSphere = new Color3f(0.0f,0.9f,0.0f);
	    Color3f diffuseColourRSphere = new Color3f(0.1f,0.9f,0.0f);
	    Color3f specularColourRSphere = new Color3f(0.1f,0.9f,0.0f);
	    float shininessRSphere = 20.0f;
	    // setting appearance (light + texture)
	    Appearance redSphereApp = new Appearance();
	    redSphereApp.setTexture(texture);
	    redSphereApp.setTextureAttributes(textAttr);
	    redSphereApp.setMaterial(new Material(ambientColourRSphere,emissiveColourRSphere,
	                             diffuseColourRSphere,specularColourRSphere,shininessRSphere));
	    
	    Sphere redSphere = new Sphere(1.0f,primflags,550,redSphereApp);

		// creating lighting and blue sphere (sun)
	    
	    Color3f ambientColourBSphere = new Color3f(0.0f,0.0f,0.6f);
	    Color3f emissiveColourBSphere = new Color3f(0.0f,0.0f,0.0f);
	    Color3f diffuseColourBSphere = new Color3f(0.0f,0.0f,0.6f);
	    Color3f specularColourBSphere = new Color3f(0.0f,0.0f,0.8f);
	    float shininessBSphere = 20.0f;
	    Appearance blueSphereApp = new Appearance();
	    blueSphereApp.setTexture(texture);
	    blueSphereApp.setTextureAttributes(textAttr);
	    blueSphereApp.setMaterial(new Material(ambientColourBSphere,emissiveColourBSphere,
	                             diffuseColourBSphere,specularColourBSphere,shininessBSphere));
	    Sphere blueSphere = new Sphere(1.0f,primflags,550,blueSphereApp);
		
	    Switch colourSwitch = new Switch();
	    colourSwitch.setCapability(Switch.ALLOW_SWITCH_WRITE);

	    // The Switch node controls which of its children will be rendered.
	    // add the spheres to the Switch.
	    colourSwitch.addChild(sun);
	    colourSwitch.addChild(redSphere);
	    colourSwitch.addChild(blueSphere); 

	    // The red sphere should be visible in the beginning.
	    colourSwitch.setWhichChild(2);
	    
		// -------------------------SUN LIGHTING--------------------------------------------
		
		// Creating lighting for the sun
		Color3f light1Color = new Color3f(1.0f,1.0f,1.0f);
		
		Vector3f light1Direction = new Vector3f(4.0f,-7.0f,-12.0f);
		DirectionalLight light1 = new DirectionalLight(light1Color,light1Direction);
		light1.setInfluencingBounds(bounds);
		sunBG.addChild(light1);
		AmbientLight ambientLight = new AmbientLight(new Color3f(1.0f,1.0f,0.0f));
		ambientLight.setInfluencingBounds(bounds);
		
		Color3f light2Color = new Color3f(1.0f,1.0f,1.0f);
		
		Vector3f light2Direction = new Vector3f(-4.0f,7.0f,12.0f);
		DirectionalLight light2 = new DirectionalLight(light2Color,light2Direction);
		light2.setInfluencingBounds(bounds);
		sunBG.addChild(light2);
		AmbientLight ambientLight2 = new AmbientLight(new Color3f(1.0f,1.0f,0.0f));
		ambientLight2.setInfluencingBounds(bounds);
		
		// adding sun to object 
		objRoot.addChild(mainTG);
		mainTG.addChild(sunBG);
		sunBG.addChild(sunTransformGroup1);
		sunTransformGroup1.addChild(colourSwitch);
		
		// --------------------------MECURY----------------------------------

		TransformGroup mercuryTransformGroup0 = new TransformGroup();
		mercuryTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D mercurySunTranform3D0 = new Transform3D();
		
		// setting rotation around the sun
		setRotationAroundSun(mercuryTransformGroup0, mercurySunTranform3D0,10000);

		Transform3D mercuryTranform3D1 = new Transform3D();
		
		// setting scale
		mercuryTranform3D1.setScale(new Vector3d(0.35, 0.35, 0.35));
		
		// setting translation
		mercuryTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 1.5));

		// applying transform to transformGroup
		TransformGroup mercuryTransformGroup1 = new TransformGroup(mercuryTranform3D1);

		TransformGroup mecuryTransformGroup2 = new TransformGroup();
		mecuryTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D mecuryTransform3D2 = new Transform3D();
		// setting y axis rotation
		setYAxisRotation(mecuryTransform3D2, mecuryTransformGroup2);

		// creating planet with image texture
		Sphere mercury = new Sphere(0.5f, getPrimitiveFlag(),550,
				getTexture("Images\\mecuryBack.jpg"));

		// Mercury
		mainTG.addChild(mercuryBG);
		mercuryBG.addChild(mercuryTransformGroup0);
		mercuryTransformGroup0.addChild(mercuryTransformGroup1);
		mercuryTransformGroup1.addChild(mecuryTransformGroup2);

		mercuryTransformGroup0.addChild(getYRotator());
		mecuryTransformGroup2.addChild(getAroundSunRotator());
		mecuryTransformGroup2.addChild(mercury);

		// -----------------------------------VENUS-------------------------------------------------------------

		TransformGroup venusTransformGroup0 = new TransformGroup();
		venusTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D venusSunTranform3D0 = new Transform3D();
		// setting rotation around the sun
		setRotationAroundSun(venusTransformGroup0, venusSunTranform3D0,12000);

		Transform3D venusTranform3D1 = new Transform3D();
		// setting scale
		venusTranform3D1.setScale(new Vector3d(0.4, 0.4, 0.4));
		// setting translation
		venusTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 2.5));

		// applying effect to transform group
		TransformGroup venusTransformGroup1 = new TransformGroup(
				venusTranform3D1);

		TransformGroup venusTransformGroup2 = new TransformGroup();
		venusTransformGroup2
				.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D venusTransform3D2 = new Transform3D();
		// setting y axis rotation
		setYAxisRotation(venusTransform3D2, venusTransformGroup2);

		// creating planet with image texture
		Sphere venus = new Sphere(0.5f, getPrimitiveFlag(), 550,
				getTexture("Images\\venusBack.jpg"));

		// Venus
		mainTG.addChild(venusBG);
		venusBG.addChild(venusTransformGroup0);
		venusTransformGroup0.addChild(venusTransformGroup1);
		venusTransformGroup1.addChild(venusTransformGroup2);

		venusTransformGroup0.addChild(getYRotator());
		venusTransformGroup2.addChild(getAroundSunRotator());
		venusTransformGroup2.addChild(venus);

		// ----------------------------------------EARTH-----------------------------------------------------------

		TransformGroup earthTransformGroup0 = new TransformGroup();
		earthTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D earthSunTranform3D0 = new Transform3D();
		// setting rotation around the sun
		setRotationAroundSun(earthTransformGroup0, earthSunTranform3D0,110000);

		Transform3D earthTranform3D1 = new Transform3D();
		// setting scale
		earthTranform3D1.setScale(new Vector3d(0.5, 0.5, 0.5));
		// setting translation
		earthTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 3.5));

		// applying effect to transform group
		TransformGroup earthTransformGroup1 = new TransformGroup(
				earthTranform3D1);

		TransformGroup earthTransformGroup2 = new TransformGroup();
		earthTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D earthTransform3D2 = new Transform3D();
		// setting y axis rotation
		setYAxisRotation(earthTransform3D2, earthTransformGroup2);

		// creating planet with image texture
		Sphere earth = new Sphere(0.5f, getPrimitiveFlag(), 550,
				getTexture("Images\\earthBack.jpg"));

		// Earth
		mainTG.addChild(earthBG);
		earthBG.addChild(earthTransformGroup0);
		earthTransformGroup0.addChild(earthTransformGroup1);
		earthTransformGroup1.addChild(earthTransformGroup2);

		earthTransformGroup0.addChild(getAroundSunRotator());
		earthTransformGroup2.addChild(getYRotator());
		earthTransformGroup2.addChild(earth);

		// -------------------------------------MOON-----------------------------------------------------------
		TransformGroup moonTransformGroup0 = new TransformGroup();
		moonTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D moonTranform3D0 = new Transform3D();
		// setting rotation around the sun
		setRotationAroundSun(moonTransformGroup0, moonTranform3D0,15000);

		Transform3D moonTranform3D1 = new Transform3D();
		// setting scale
		moonTranform3D1.setScale(new Vector3d(0.1, 0.1, 0.1));
		// setting translation
		moonTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 0.6));

		// applying effect to transform group
		TransformGroup moonTransformGroup1 = new TransformGroup(moonTranform3D1);

		TransformGroup moonTransformGroup2 = new TransformGroup();
		moonTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D moonTransform3D2 = new Transform3D();
		// setting y axis rotation
		setYAxisRotation(moonTransform3D2, moonTransformGroup2);

		// creating planet with image texture
		Sphere moon = new Sphere(0.5f, getPrimitiveFlag(), 550,
				getTexture("Images\\moonBack.jpg"));

		// adding moon to earth transform group
		earthTransformGroup2.addChild(moonTransformGroup0);

		moonTransformGroup0.addChild(moonTransformGroup1);
		moonTransformGroup1.addChild(moonTransformGroup2);

		moonTransformGroup0.addChild(getAroundSunRotator());
		moonTransformGroup2.addChild(getYRotator());
		moonTransformGroup2.addChild(moon);

		// -------------------------------MARS-------------------------------------------------------------------
		TransformGroup marsTransformGroup0 = new TransformGroup();
		marsTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D marsSunTranform3D0 = new Transform3D();
		// setting rotation around the sun
		setRotationAroundSun(marsTransformGroup0, marsSunTranform3D0,16000);

		Transform3D marsTranform3D1 = new Transform3D();
		// setting scale
		marsTranform3D1.setScale(new Vector3d(0.4, 0.4, 0.4));
		// setting translation
		marsTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 4.5));

		// applying effect to transform group
		TransformGroup marsTransformGroup1 = new TransformGroup(marsTranform3D1);

		TransformGroup marsTransformGroup2 = new TransformGroup();
		marsTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D marsTransform3D2 = new Transform3D();
		// setting y axis rotation
		setYAxisRotation(marsTransform3D2, marsTransformGroup2);

		// creating planet with image texture
		Sphere mars = new Sphere(0.5f, getPrimitiveFlag(), 550,getTexture("Images\\marsBack.jpg"));

		// Mars
		mainTG.addChild(marsBG);
		marsBG.addChild(marsTransformGroup0);
		marsTransformGroup0.addChild(marsTransformGroup1);
		marsTransformGroup1.addChild(marsTransformGroup2);

		marsTransformGroup0.addChild(getYRotator());
		marsTransformGroup2.addChild(getAroundSunRotator());
		marsTransformGroup2.addChild(mars);

		// ---------------------------------JUPITORS--------------------------------------------------------------
		TransformGroup jupitorTransformGroup0 = new TransformGroup();
		jupitorTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D jupitorSunTranform3D0 = new Transform3D();
		// setting rotation around the sun
		setRotationAroundSun(jupitorTransformGroup0, jupitorSunTranform3D0,12050);

		Transform3D jupitorTranform3D1 = new Transform3D();
		// setting scale
		jupitorTranform3D1.setScale(new Vector3d(1.0, 1.0, 1.0));
		// setting translation
		jupitorTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 5.5));

		// applying effect to transform group
		TransformGroup jupitorTransformGroup1 = new TransformGroup(jupitorTranform3D1);

		TransformGroup jupitorTransformGroup2 = new TransformGroup();
		jupitorTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D jupitorTransform3D2 = new Transform3D();
		// setting y axis rotation
		setYAxisRotation(jupitorTransform3D2, jupitorTransformGroup2);

		// creating planet with image texture
		Sphere jupitor = new Sphere(0.5f, getPrimitiveFlag(), 550,getTexture("Images\\jupitorBack.jpg"));

		// Jupitor
		mainTG.addChild(jupitorBG);
		jupitorBG.addChild(jupitorTransformGroup0);
		jupitorTransformGroup0.addChild(jupitorTransformGroup1);
		jupitorTransformGroup1.addChild(jupitorTransformGroup2);

		jupitorTransformGroup0.addChild(getYRotator());
		jupitorTransformGroup2.addChild(getAroundSunRotator());
		jupitorTransformGroup2.addChild(jupitor);

		// ------------------------------------------SATURN--------------------------------------------------------

		TransformGroup saturnTransformGroup0 = new TransformGroup();
		saturnTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D saturnSunTranform3D0 = new Transform3D();
		// setting rotation around the sun
		setRotationAroundSun(saturnTransformGroup0, saturnSunTranform3D0,14049);

		Transform3D saturnTranform3D1 = new Transform3D();
		// setting scale
		saturnTranform3D1.setScale(new Vector3d(1.0, 1.0, 1.0));
		// setting translation
		saturnTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 7.5));

		// applying effect to transform group
		TransformGroup saturnTransformGroup1 = new TransformGroup(
				saturnTranform3D1);

		TransformGroup saturnTransformGroup2 = new TransformGroup();
		saturnTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D saturnTransform3D2 = new Transform3D();
		// setting y axis rotation
		setYAxisRotation(saturnTransform3D2, saturnTransformGroup2);

		// creating planet with image texture
		Sphere saturn = new Sphere(0.5f, getPrimitiveFlag(), 550,getTexture("Images\\saturnBack.jpg"));

		mainTG.addChild(saturnBG);
		saturnBG.addChild(saturnTransformGroup0);
		saturnTransformGroup0.addChild(saturnTransformGroup1);
		saturnTransformGroup1.addChild(saturnTransformGroup2);

		saturnTransformGroup0.addChild(getYRotator());
		saturnTransformGroup2.addChild(getAroundSunRotator());
		saturnTransformGroup2.addChild(saturn);

		// ring around saturn
		TransformGroup ringTransformGroup0 = new TransformGroup();
		TransformGroup ringHolowTransformGroup1 = new TransformGroup();

		// creating thin cylinder with light image texture
		Cylinder ring = new Cylinder(0.8f, 0.05f,550,getTexture("Images\\saturnRingBack.jpg"));
		// creating thin cylinder to give ring like effect
		Cylinder hollowSectionOfRing = new Cylinder(0.6f, 0.05f,550,getTexture("Images\\hollowSectionBack.jpg"));

		// adding to saturn planet
		saturnTransformGroup2.addChild(ringTransformGroup0);
		ringTransformGroup0.addChild(ringHolowTransformGroup1);

		ringTransformGroup0.addChild(ring);
		ringHolowTransformGroup1.addChild(hollowSectionOfRing);

		// ----------------------------------URANUS------------------------------------------------------------

		TransformGroup uranusTransformGroup0 = new TransformGroup();
		uranusTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D uranusSunTranform3D0 = new Transform3D();
		// setting rotation around the sun
		setRotationAroundSun(uranusTransformGroup0, uranusSunTranform3D0,19080);

		Transform3D uranusTranform3D1 = new Transform3D();
		// setting scale
		uranusTranform3D1.setScale(new Vector3d(0.8, 0.8, 0.8));
		// setting translation
		uranusTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 9.5));

		// applying effect to transform group
		TransformGroup uranusTransformGroup1 = new TransformGroup(uranusTranform3D1);

		TransformGroup uranusTransformGroup2 = new TransformGroup();
		uranusTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D uranusTransform3D2 = new Transform3D();
		// setting y axis rotation
		setYAxisRotation(uranusTransform3D2, uranusTransformGroup2);

		// creating planet with image texture
		Sphere uranus = new Sphere(0.5f, getPrimitiveFlag(), 550,getTexture("Images\\uranusBack.jpg"));

		// Uranus
		mainTG.addChild(uranusBG);
		uranusBG.addChild(uranusTransformGroup0);
		uranusTransformGroup0.addChild(uranusTransformGroup1);
		uranusTransformGroup1.addChild(uranusTransformGroup2);

		uranusTransformGroup0.addChild(getYRotator());
		uranusTransformGroup2.addChild(getAroundSunRotator());
		uranusTransformGroup2.addChild(uranus);
		// -------------------------------------NEPTUNE-----------------------------------------------------------

		TransformGroup neptuneTransformGroup0 = new TransformGroup();
		neptuneTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D neptuneSunTranform3D0 = new Transform3D();
		// setting rotation around the sun
		setRotationAroundSun(neptuneTransformGroup0, neptuneSunTranform3D0,140000);

		Transform3D neptuneTranform3D1 = new Transform3D();
		// setting scale
		neptuneTranform3D1.setScale(new Vector3d(0.8, 0.8, 0.8));
		// setting translation
		neptuneTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 10.5));

		// applying effect to transform group
		TransformGroup neptuneTransformGroup1 = new TransformGroup(neptuneTranform3D1);

		TransformGroup neptuneTransformGroup2 = new TransformGroup();
		neptuneTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D neptuneTransform3D2 = new Transform3D();
		// setting y axis rotation
		setYAxisRotation(neptuneTransform3D2, neptuneTransformGroup2);

		// creating planet with image texture
		Sphere neptune = new Sphere(0.5f, getPrimitiveFlag(), 550,getTexture("Images\\neptuneBack.jpg"));

		// Neptune
		mainTG.addChild(neptuneBG);
		neptuneBG.addChild(neptuneTransformGroup0);
		neptuneTransformGroup0.addChild(neptuneTransformGroup1);
		neptuneTransformGroup1.addChild(neptuneTransformGroup2);

		neptuneTransformGroup0.addChild(getYRotator());
		neptuneTransformGroup2.addChild(getAroundSunRotator());
		neptuneTransformGroup2.addChild(neptune);

		// ----------------------------------PLUTO--------------------------------------------------------------
		
		TransformGroup plutoTransformGroup0 = new TransformGroup();
		plutoTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D plutoSunTranform3D0 = new Transform3D();
		// setting rotation around the sun
		setRotationAroundSun(plutoTransformGroup0, plutoSunTranform3D0,160000);

		Transform3D plutoTranform3D1 = new Transform3D();
		// setting scale
		plutoTranform3D1.setScale(new Vector3d(0.3, 0.3, 0.3));
		// setting translation
		plutoTranform3D1.setTranslation(new Vector3d(0.0, 0.0, 12.0));

		// applying effect to transform group
		TransformGroup plutoTransformGroup1 = new TransformGroup(plutoTranform3D1);

		TransformGroup plutoTransformGroup2 = new TransformGroup();
		plutoTransformGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Transform3D plutoTransform3D2 = new Transform3D();
		// setting y axis rotation
		setYAxisRotation(plutoTransform3D2, plutoTransformGroup2);

		// creating planet with image texture
		Sphere pluto = new Sphere(0.4f, getPrimitiveFlag(),550,getTexture("Images\\plutoBack.jpg"));

		// adding transform to mainTG and correct transform group
		mainTG.addChild(plutoBG);
		plutoBG.addChild(plutoTransformGroup0);
		plutoTransformGroup0.addChild(plutoTransformGroup1);
		plutoTransformGroup1.addChild(plutoTransformGroup2);

		plutoTransformGroup0.addChild(getYRotator());
		plutoTransformGroup2.addChild(getAroundSunRotator());
		plutoTransformGroup2.addChild(pluto);
		// ----------------------------------------ROCKET------------------------------------------------------
	
		// creating lighting and texture
		Color3f ambientColourRocket = new Color3f(1.0f, 1.0f, 1.1f);
		Color3f emissiveColourRocket = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f diffuseColourRocket = new Color3f(1.0f, 1.0f, 1.1f);
		Color3f specularColourRocket = new Color3f(1.0f, 0.0f, 1.1f);
		float shininessRocket = 160.0f;
		
		Appearance RocketApp = new Appearance();
		
		// material for the rocket
		RocketApp.setMaterial(new Material(ambientColourRocket,
				emissiveColourRocket, diffuseColourRocket,
				specularColourRocket, shininessRocket));
		
		// rocket shell (outer body/cylinder)
		Transform3D rocketTF = new Transform3D();
		rocketTF.setTranslation(new Vector3f(4.0f,0.0f,0.0f));
		TransformGroup rocketTG1 = new TransformGroup(rocketTF);
		Cylinder rocketShell = new Cylinder(0.1f,0.50f,RocketApp);
		
		// rocket nose (cone)
		Transform3D rocketNoseTF = new Transform3D();
		rocketNoseTF.setTranslation(new Vector3f(0.0f,0.30f,0.0f));
		TransformGroup rocketTG2 = new TransformGroup(rocketNoseTF);
		Cone rocketNose = new Cone(0.1f,0.1f,RocketApp);
		
		// rocket winds (thin pieces of square in X shape position)
		Transform3D rocketWing1TF = new Transform3D();
		rocketWing1TF.setTranslation(new Vector3f(0.0f,-0.20f,0.0f));
		TransformGroup wingTG1 = new TransformGroup(rocketWing1TF);
		Box wing1 = new Box(0.20f,0.04f,0.01f,RocketApp);
	
		Transform3D rocketWing2TF = new Transform3D();
		rocketWing2TF.rotY(Math.PI/2);
		rocketWing2TF.setTranslation(new Vector3f(0.0f,-0.20f,0.0f));
		TransformGroup wingTG3 = new TransformGroup(rocketWing2TF);
		Box wing3 = new Box(0.20f,0.04f,0.01f,RocketApp);
		
		Transform3D rocketSunTranform3D0 = new Transform3D();
		
		// setting rotation around the sun so its in a upwards position 
		rocketSunTranform3D0.rotX(Math.PI /2);
		
		TransformGroup rocketTransformGroup0 = new TransformGroup(rocketSunTranform3D0);
		rocketTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		// rotation of the rocket around the sun
		RotationInterpolator RotatorRocket = new RotationInterpolator(new Alpha(-1, 8000),
				rocketTransformGroup0, rocketSunTranform3D0, 0.0f, (float) Math.PI * 2);
		
		RotatorRocket.setSchedulingBounds(bounds);
		
		mainTG.addChild(rocketBG);
		rocketBG.addChild(rocketTransformGroup0);
		rocketTransformGroup0.addChild(rocketTG1);
		rocketTransformGroup0.addChild(RotatorRocket);
		rocketTG1.addChild(rocketShell);
		rocketTG1.addChild(rocketTG2);
		
		rocketTG2.addChild(rocketNose);
		
		rocketTG1.addChild(wingTG1);
		rocketTG1.addChild(wingTG3);
		wingTG1.addChild(wing1);
		wingTG3.addChild(wing3);
				
		// --------------------------------------------UFO-----------------------------------------------------
		
		// setting up lighting 
		Color3f ambientColourUFO = new Color3f(1.0f, 1.0f, 1.1f);
		Color3f emissiveColourUFO = new Color3f(0.0f, 0.0f, 0.0f);
		Color3f diffuseColourUFO = new Color3f(1.0f, 1.0f, 1.1f);
		Color3f specularColourUFO = new Color3f(1.0f, 0.0f, 1.1f);
		float shininessUFO = 160.0f;
		
		Appearance UFOApp = new Appearance();
		
		// setting up material
		UFOApp.setMaterial(new Material(ambientColourUFO,
				emissiveColourUFO, diffuseColourUFO,
				specularColourUFO, shininessUFO));

		Transform3D UFOTranform3D1 = new Transform3D();
		// setting scale
		UFOTranform3D1.setScale(new Vector3d(0.5, 0.5, 0.5));
		// setting translation
		UFOTranform3D1.setTranslation(new Vector3d(5.0, 2.0, 0.0));

		// applying effect to transform group
		TransformGroup UFOTransformGroup0 = new TransformGroup(UFOTranform3D1);
		UFOTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		UFOTransformGroup0.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		UFOTransformGroup0.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

		// creating planet with image texture
		Sphere UFO = new Sphere(0.3f, getPrimitiveFlag(),550,UFOApp);

		// UFO
		mainTG.addChild(UFOTransformGroup0);
		UFOTransformGroup0.addChild(UFO);

		// thin disk in the middle of the ufo
		TransformGroup UFOringTransformGroup0 = new TransformGroup();

		// adding the disk (cylinder) with a texture
		Cylinder UFOring = new Cylinder(0.6f, 0.04f,550,
				getTexture("Images\\UFOBack.jpg"));

		UFOTransformGroup0.addChild(UFOringTransformGroup0);

		UFOringTransformGroup0.addChild(UFOring);
		
		//-----------------------COLLISION DETECTION-----------------------------
	    
	    // creating collition bounds for the UFO and the sun so when the UFO goes in the bounds it wakes up
	    float radius = 1.0f;
	    double kk = 2.0;
	    UFOTransformGroup0.setCollisionBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0),kk*radius));
	    UFOTransformGroup0.setCollidable(true);
	    
	    double k = 1.0 ; 
	    colourSwitch.setCollisionBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0),k*radius)); 
	    //Enabled for collision purposes
	    colourSwitch.setCollidable(true);
	    
	    //CollisionBehaviour1 class takes care of changing the colour of the sphere when the ufo touches it.
	    CollisionBehaviour1 scb1 = new CollisionBehaviour1(UFO,colourSwitch,bounds);
	    mainTG.addChild(scb1);
	    
	    //-----------------------BACKGROUND (STARS)-----------------------------------
		
	    // creating a star background 
		TextureLoader myLoader = new TextureLoader("Images\\stars.jpg", this);
		ImageComponent2D myImage = myLoader.getImage();
		Background myBack = new Background();
		myBack.setImage(myImage);
		myBack.setApplicationBounds(bounds);
		myBack.setCapability(Background.ALLOW_IMAGE_WRITE);
		objRoot.addChild(myBack);
		
		//In order to allow navigation through the scene with the keyboard,
	    //everything must be collected in a separate transformation group to which 
	    //the KeyNavigatorBehavior is applied.
	    KeyNavigatorBehavior knb = new KeyNavigatorBehavior(mainTG);
	    knb.setSchedulingBounds(bounds);
	    mainTG.addChild(knb);
	    // The PickTranslateBehavior for moving the UFO.
	    PickTranslateBehavior pickTrans = new PickTranslateBehavior(objRoot,c,bounds);
	    objRoot.addChild(pickTrans); 

		MouseRotate behavior = new MouseRotate();
		behavior.setTransformGroup(mainTG);
		objRoot.addChild(behavior);
		behavior.setSchedulingBounds(bounds);

		MouseZoom behavior2 = new MouseZoom();
		behavior2.setTransformGroup(mainTG);
		objRoot.addChild(behavior2);
		behavior2.setSchedulingBounds(bounds);

//		MouseTranslate behavior3 = new MouseTranslate();
//		behavior3.setTransformGroup(mainTG);
//		objRoot.addChild(behavior3);
//		behavior3.setSchedulingBounds(bounds);

		objRoot.compile();
		return objRoot;
	}
	// method sets the rotation around the sun, it add the Rotation Interpolator to the transformation group  
	public void setRotationAroundSun(TransformGroup transformGroup,
			Transform3D transform3D,int speed) {
		Rotator0 = new RotationInterpolator(new Alpha(-1, speed),
				transformGroup, transform3D, 0.0f, (float) Math.PI * 2);
		Rotator0.setSchedulingBounds(bounds);
	}

	public static void main(String[] args) {
		new MainFrame(new SolarSystem(), 512, 512);
	}
	
	// return the Y RotationInterpolator
	public RotationInterpolator getYRotator() {
		return Rotator0;
	}
	
	// method sets the Y Axis rotation, it add the Rotation Interpolator to the transformation group  
	public void setYAxisRotation(Transform3D transform3D,
			TransformGroup transformGroup) {
		
		// creating RotationInterpolator with Alpha (speed) 
		Rotator1 = new RotationInterpolator(new Alpha(-1, 1900),
				transformGroup, transform3D, 0.0f, (float) Math.PI * (2.0f));
		Rotator1.setSchedulingBounds(bounds);
	}
	// get the RotationInterpolator for the around the sun effect
	public RotationInterpolator getAroundSunRotator() {
		return Rotator1;
	}
	// method to create a image texture, returns a appear which can be used in all primitive object
	public Appearance getTexture(String imgPath) {
		TextureLoader TextureLoader0 = new TextureLoader(imgPath,
				new Container());
		Texture Texture0 = TextureLoader0.getTexture();
		Texture0.setBoundaryModeS(Texture.WRAP);
		Texture0.setBoundaryModeT(Texture.WRAP);
		Texture0.setBoundaryColor(new Color4f(0.0f, 1.0f, 0.0f, 0.0f));
		TextureAttributes TextureAttribute0 = new TextureAttributes();

		Appearance appearance = new Appearance();
		appearance.setTexture(Texture0);
		appearance.setTextureAttributes(TextureAttribute0);

		return appearance;
	}

	public int getPrimitiveFlag() {
		int primFlag = Primitive.GENERATE_NORMALS
				+ Primitive.GENERATE_TEXTURE_COORDS;
		return primFlag;
	}
	// method to add light 
	protected void addLights(BranchGroup b) {
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
				100.0);
		// Set up the ambient light
		Color3f ambientColour = new Color3f(0.2f, 0.2f, 0.2f);
		AmbientLight ambientLight = new AmbientLight(ambientColour);
		ambientLight.setInfluencingBounds(bounds);
		// Set up the directional light
		Color3f lightColour = new Color3f(1.0f, 1.0f, 1.0f);
		Vector3f lightDir = new Vector3f(-1.0f, -1.0f, -1.0f);
		DirectionalLight light = new DirectionalLight(lightColour, lightDir);
		light.setInfluencingBounds(bounds);
		// Add the lights to the BranchGroup
		b.addChild(ambientLight);
		b.addChild(light);
	}

}
