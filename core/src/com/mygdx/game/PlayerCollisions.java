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

                if (fixA.getBody().getUserData().toString() == "falling") {
                    falling = fixA;
                } else if (fixB.getBody().getUserData().toString() == "falling") {
                    falling = fixB;
                }

                if (fixA.getBody().getUserData().toString() == "Travis") {
                    travis = fixA;
                    other = fixB;
                } else if (fixB.getBody().getUserData().toString() == "Travis") {
                    travis = fixB;
                    other = fixA;
                }

                if (falling != null) {
                    
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

                //do collide here
                if (fixA.getBody().getUserData() == "Travis") {
                    System.out.println(fixA.getBody().getUserData() + " has collided with " +  fixB.getBody().getType());
                } else if (fixB.getBody().getUserData() == "Travis") {
                    System.out.println(fixB.getBody().getUserData() + " has collided with " +  fixA.getBody().getType());
                } else {
                System.out.println(fixA.getBody().getType() + " has collided with " + fixB.getBody().getType());
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixA = contact.getFixtureA();
                Fixture fixB = contact.getFixtureB();
                Fixture travis;
                Fixture other;

                if (fixA.getBody().getUserData() == "Travis") {
                    travis = fixA;
                    other = fixB;
                } else if (fixB.getBody().getUserData() == "Travis") {
                    travis = fixB;
                    other = fixA;
                }

                //do no more collide
                if (fixA.getBody().getUserData() == "Travis") {
                    System.out.println(fixA.getBody().getUserData() + " has stopped collide with " +  fixB.getBody().getType());
                } else if (fixB.getBody().getUserData() == "Travis") {
                    System.out.println(fixB.getBody().getUserData() + " has stopped collide with " +  fixA.getBody().getType());
                } else {
                System.out.println(fixA.getBody().getType() + " has stopped collide with " + fixB.getBody().getType());
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
