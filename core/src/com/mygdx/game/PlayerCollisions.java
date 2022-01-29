package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PlayerCollisions {
    
    public static ContactListener createCollisionListener() {
        ContactListener listener = new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                Fixture fixA = contact.getFixtureA();
                Fixture fixB = contact.getFixtureB();
                Fixture travis = null;
                Fixture other = null;
                Fixture falling = null;

                if (fixA.getBody().getUserData() != null && fixB.getBody().getUserData() != null) {
                    if (fixA.getBody().getUserData().toString() == "falling") {
                        falling = fixA;
                        other = fixB;
                    } else if (fixB.getBody().getUserData().toString() == "falling") {
                        falling = fixB;
                        other = fixA;
                    }
                    /*
                    if (falling != null && falling.getBody().getUserData() instanceof FallingTile) {
                        if (falling.untouched && other.getBody().getUserData().toString() == "Travis") {
                            falling.fall();
                        } else if (!falling.untouched && (other.getBody().getUserData() instanceof Tile) && (!other.getBody().getUserData() instanceof Spike)) {
                            falling.poof();
                        }
                    }
                    */
                    if (fixA.getBody().getUserData().toString() == "Travis") {
                        travis = fixA;
                        other = fixB;
                    } else if (fixB.getBody().getUserData().toString() == "Travis") {
                        travis = fixB;
                        other = fixA;
                    }

                    if (travis != null && travis.getBody().getUserData() instanceof Player) {
                        Player trueTravis = (Player) travis.getBody().getUserData();
                        switch (other.getBody().getUserData().toString()) {
                            case "sticky" :
                                trueTravis.sticky = true;
                                System.out.println("Such stick,  Wow");
                            break;
                            case "bouncy" :
                                //trueTravis.bounce();
                                System.out.println("Boing!");
                            break;
                            case "sliderleft" :
                                //trueTravis.sliderleft;
                                System.out.println("<-----");
                            break;
                            case "sliderright" :
                                //trueTravis.sliderright;
                                System.out.println("----->");
                            break;
                            case "gravityup" :
                                trueTravis.gravityDirection = "up";
                                System.out.println("Up We Go");
                            break;
                            case "spike" :
                                //call reset method?
                                System.out.println("Ouch!");
                            break;

                        }
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixA = contact.getFixtureA();
                Fixture fixB = contact.getFixtureB();
                Fixture travis = null;
                Fixture other = null;


                if (fixA.getBody().getUserData() != null && fixB.getBody().getUserData() != null) {
                    if (fixA.getBody().getUserData() == "Travis") {
                        travis = fixA;
                        other = fixB;
                    } else if (fixB.getBody().getUserData() == "Travis") {
                        travis = fixB;
                        other = fixA;
                    }

                    if (travis != null && travis.getBody().getUserData() instanceof Player) {
                        Player trueTravis = (Player) travis.getBody().getUserData();
                        switch (other.getBody().getUserData().toString()) {
                            case "sticky" :
                                trueTravis.sticky = false;
                                System.out.println("No more stick");
                            break;
                            //bouncy only needs to get called once, I suspect, and doesn't need an end condition
                            //case "bouncy" :
                                //trueTravis.bounce();
                            //    System.out.println("Boing!");
                            //break;
                            case "sliderleft" :
                                //trueTravis.sliderleft;
                                System.out.println("<--x--");
                            break;
                            case "sliderright" :
                                //trueTravis.sliderright;
                                System.out.println("--x-->");
                            break;
                            case "gravityup" :
                                trueTravis.gravityDirection = "down";
                                System.out.println("Back down");
                            break;
                            //since the case resets, and probably calls a method, it shouldnt need an end case
                            //case "spike" :
                                //call reset method?
                            //    System.out.println("Ouch!");
                            //break;

                        }
                    }
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                //Don't know what this does, but it has to be here to implement ContactListener
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                //Don't know what this does, but it has to be here to implement ContactListener
            }
        };
        return listener;
        }

}
